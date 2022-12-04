import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.time.temporal.ChronoUnit.SECONDS;

public class APIHandle {

    // Singleton Class

    private static APIHandle apiHandle;

    private String access_token;
    private String refresh_token;

    // Cached Data from the API
    private int notificationsNum;
    private List<Map<String,String>> notifications = new ArrayList<>();
    private List<Map<String,String>> allnotifications = new ArrayList<>();

    private final String apiURI = "http://139.162.185.53:8080/";
//    private final String apiURI = "http://127.0.0.1:8080/";

    public static APIHandle getInstance(){
        if(apiHandle == null)
            apiHandle = new APIHandle();
        return apiHandle;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getNotificationsNum() {
        return notificationsNum;
    }

    public List<Map<String, String>> getNotifications() {
        return notifications;
    }

    public String getApiURI() {
        return apiURI;
    }

    public HttpRequest.Builder request(String path, Boolean auth) throws URISyntaxException, IOException, InterruptedException {
        String uri = apiURI + path;
        HttpRequest.Builder request = HttpRequest.newBuilder(new URI(uri)).timeout(Duration.of(10, SECONDS));
        if(auth) request = request.header("Authorization", "Bearer ".concat(this.getAccess_token()));
        return request;
    }

    public List<Map<String,String>>  refreshNotifications(){
        try {
            String path = "banker/getNotifications";
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.GET().header("Accept", "application/json").build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            this.notifications = mapper.readValue(JSON, List.class);
            this.notificationsNum = this.notifications.size();
            return this.notifications;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getAccountsByName(String firstName, String lastName){
        try {
            String query = String.format("?firstName=%s&lastName=%s",firstName.strip(), lastName.strip());
            String path = "banker/getAccountsByName".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.GET().header("Accept", "application/json").build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(JSON, List.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public Map<String, String> getAccountsByNumber(String number){
        try {
            String query = String.format("?number=%s",number.strip());
            String path = "banker/getAccountsByAccount".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.GET().header("Accept", "application/json").build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(JSON, Map.class);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Map<String, String>> getStatement(String accountNumber){
        try {
            String query = String.format("?accountNumber=%s",accountNumber.strip());
            String path = "banker/requestStatement".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.GET().header("Accept", "application/json").build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> map = mapper.readValue(JSON, List.class);
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void approveLoan(String accountNumber, String loanID){
        try {
            String query = String.format("?accountNumber=%s&loanID=%s",accountNumber.strip(), loanID);
            String path = "banker/approveLoan".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void disproveLoan(String accountNumber, String loanID){
        try {
            String query = String.format("?accountNumber=%s&loanID=%s",accountNumber.strip(), loanID);
            String path = "banker/disproveLoan".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void payInterest(String interestAmount){
        try {
            String query = String.format("?interestAmount=%s",interestAmount.strip());
            String path = "banker/payInterest".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> getLoans(String accountNumber){
        try {
            String query = String.format("?accountNumber=%s",accountNumber.strip());
            String path = "banker/getLoans".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.GET().build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> map = mapper.readValue(JSON, List.class);
            return map;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void approveAccount(String accountNumber){
        try {
            String query = String.format("?accountNumber=%s",accountNumber.strip());
            String path = "banker/approveAccount".concat(query);
            HttpRequest.Builder req = request(path, true);
            HttpRequest request = req.POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean signin(String username, String password){
        try {
            String query = String.format("?username=%s&password=%s", username, password);
            String path = "signin".concat(query);
            HttpRequest.Builder req = request(path, false);
            HttpRequest request = req.POST(HttpRequest.BodyPublishers.ofString("")).build();
            HttpClient httpClient = HttpClient.newHttpClient();
            CompletableFuture<HttpResponse<String>> cf = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            HttpResponse<String> res = cf.join();
            if(res.statusCode() == 403) return false;
            String JSON = res.body();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(JSON, Map.class);
            this.access_token = map.get("access_token");
            this.refresh_token = map.get("refresh_token");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}