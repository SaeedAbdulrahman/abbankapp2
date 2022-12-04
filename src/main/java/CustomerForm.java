import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class CustomerForm extends JFrame {
    private JPanel panel;
    private JLabel welcome;
    private JButton HoldAccount;
    private JButton removeAccount;
    private JButton statementByName;
    private JButton searchByAccount;
    private JButton searchByName;
    private JButton logoutButton;
    private JButton notificationsButton;
    private JLabel todayDate;
    private JButton setInterestRate;
    private JPanel accountsPanel;
    private JButton left;
    private JButton right;

    private boolean interestRate = false;

    public CustomerForm() {
        super("ABBank Online Banking");
        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getImage("banker_background.png")));
        this.setContentPane(new JLabel(backgroundImage));
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(995, 588);
        panel.setOpaque(false);
        this.add(panel);
        this.setResizable(false);
        this.setVisible(true);
        APIHandle apiHandle = APIHandle.getInstance();
        DateFormat Date = DateFormat.getDateInstance();
        Calendar cals = Calendar.getInstance();
        String currentDate = Date.format(cals.getTime());
        this.todayDate.setText(currentDate);
        apiHandle.refreshNotifications();
        updateInformation(apiHandle);
        Thread t1 = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                apiHandle.refreshNotifications();
                updateInformation(apiHandle);
                System.out.println("Refreshed API!");
            }
        });
        t1.start();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        notificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new NotificationPanel();
            }
        });
        searchByName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String fullname = JOptionPane.showInputDialog(null, "Enter the user's first and last name separated by space! (Ex: Saeed Abdulrahman)");
                String firstname = fullname.split("\\s+")[0];
                String lastname = fullname.split("\\s+")[1];
                List<Map<String, String>> accounts = apiHandle.getAccountsByName(firstname, lastname);
                String[] options = new String[accounts.size()];
                int choice = JOptionPane.showOptionDialog(null, "Choose the account you would like to get its information", "Choose account",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                Map<String, String> account = accounts.get(choice);
                String number = account.get("accountNumber");
                String type = account.get("accountType");
                String balance = account.get("accountBalance");
                String debt = account.get("accountDebt");
                String approval = account.get("accountApproval");
                String creation = account.get("accountCreation");
                String signature = account.get("bankerSignature");
                Runnable r = () -> {
                    String html = "<html><body width='%1s'><h1>"+number+"</h1>"
                            + "<p><b>Account Type: " + type.toUpperCase() + "<br>"
                            + "<b>Account Balance:</b> " + balance+ "<br>"
                            + "<b>Account Debt:</b> " + debt+ "<br>"
                            + "<br><br>"
                            + "<b>Account Approval:</b> " + approval+ "<br>"
                            + "<b>Account Creation:</b> " + creation+ "<br>"
                            + "<b>Banker Signature:</b> " + signature+ "<br>"
                            + "</p>";
                    int w = 175;
                    JOptionPane.showMessageDialog(null, String.format(html, w, w));
                };
                SwingUtilities.invokeLater(r);
            }
        });
        searchByAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String accountnum = JOptionPane.showInputDialog(null, "Enter the account's number! (Ex: 432112345647859)");
                Map<String, String> account = apiHandle.getAccountsByNumber(accountnum);
                String number = account.get("accountNumber");
                String type = account.get("accountType");
                String balance = account.get("accountBalance");
                String debt = account.get("accountDebt");
                String approval = account.get("accountApproval");
                String creation = account.get("accountCreation");
                String signature = account.get("bankerSignature");
                Runnable r = () -> {
                    String html = "<html><body width='%1s'><h1>"+number+"</h1>"
                            + "<p><b>Account Type: " + type.toUpperCase() + "<br>"
                            + "<b>Account Balance:</b> " + balance+ "<br>"
                            + "<b>Account Debt:</b> " + debt+ "<br>"
                            + "<br><br>"
                            + "<b>Account Approval:</b> " + approval+ "<br>"
                            + "<b>Account Creation:</b> " + creation+ "<br>"
                            + "<b>Banker Signature:</b> " + signature+ "<br>"
                            + "</p>";
                    int w = 175;
                    JOptionPane.showMessageDialog(null, String.format(html, w, w));
                };
                SwingUtilities.invokeLater(r);
            }
        });
        statementByName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new StatementForm();
            }
        });
        HoldAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String accountNumber = JOptionPane.showInputDialog(null, "Enter the account number that you want to approve! (Ex: 43211234567898)");
                apiHandle.approveAccount(accountNumber);
            }
        });
        removeAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String accountNumber = JOptionPane.showInputDialog(null, "Enter the account number that you want to view its loans! (Ex: 43211234567898)");
                List<Map<String,String>> loans = apiHandle.getLoans(accountNumber);
                String[] options = new String[loans.size()];
                String[] reference = new String[loans.size()];
                final int[] i = {0};
                loans.forEach((loan) -> {
                    String amount = loan.get("loanAmount");
                    reference[i[0]] = loan.get("loanID");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date date;
                    try {
                        date = df.parse(loan.get("loanDate"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    String loanDate = String.valueOf(cal.getTime());
                    String option = String.format("`%s` AED - `%s`", amount, loanDate);
                    options[i[0]] = option;
                    i[0]++;
                });
                int from = JOptionPane.showOptionDialog(null, "Select the loan that you want to review:", "Choose loan",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                String loanID = reference[from];
                int choice = JOptionPane.showOptionDialog(null, "What would you like to do?", "Choose option",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Approve", "Deny"}, options[0]);
                if(choice==0)
                    apiHandle.approveLoan(accountNumber, loanID);
                else
                    apiHandle.disproveLoan(accountNumber, loanID);
            }
        });
        setInterestRate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String interestAmount = JOptionPane.showInputDialog(null, "Enter the amount of interest as a number between 0 & 1 (Ex. 0.05)");
                if(Double.parseDouble(interestAmount)>1||Double.parseDouble(interestAmount)<0){
                    JOptionPane.showMessageDialog(null, "Invalid Input!");
                    return;
                }
                Thread t1 = new Thread(() -> {
                    while (interestRate) {
                        try {
                            Thread.sleep(50000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        apiHandle.payInterest(interestAmount);
                    }
                });
                if(Double.parseDouble(interestAmount) < 0.01){
                    interestRate=false;
                }else {
                    t1.start();
                    interestRate=true;
                }
            }
        });
    }

    public Number getNumber(String totalDebts) {
        String formatted = totalDebts.split("\\s+")[0];
        DecimalFormat df = new DecimalFormat("#,##0.00");
        Number r = null;
        try {
            r = df.parse(formatted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public boolean isNumeric(String number) {
        if (number == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(number);
            if (d <= 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private void updateInformation(APIHandle handle) {
        notificationsButton.setText("`" + handle.getNotificationsNum() + "` Notifications");
    }

    private Image getImage(String filename) {
        try {
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(
                    "/" + filename)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
