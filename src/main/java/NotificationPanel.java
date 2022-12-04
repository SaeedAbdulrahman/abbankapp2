import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class NotificationPanel extends JFrame {
    private JPanel Controls;
    private JScrollPane notificationPane;
    private JScrollBar scrollBar1;
    private JLabel number;
    private JPanel panel;

    public NotificationPanel() {
        super("ABBank Notifications");
        this.setSize(970, 600);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        APIHandle apiHandle = APIHandle.getInstance();
        JPanel notificationView = new JPanel();
        notificationView.setLayout(new BoxLayout(notificationView, BoxLayout.Y_AXIS));
        List<Map<String, String>> undone = apiHandle.refreshNotifications();
        showStatements(undone, notificationView);
        number.setText("`" + apiHandle.getNotificationsNum() + "` unseen to be reviewed");
        JFrame parent = this;
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                parent.dispose();
            }
        });
    }

    public void showStatements(List<Map<String, String>> statements, JPanel spanel) {
        if (statements == null) return;
        Collections.reverse(statements);
        statements.forEach((statement) -> {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date;
            try {
                date = df.parse(statement.get("logDate"));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Notifications notificationPanel = new Notifications(cal.getTime().toString(), statement.get("logType"), statement.get("logMessage"));
            spanel.add(notificationPanel.getPanel());
        });
        notificationPane.setViewportView(spanel);
    }

}
