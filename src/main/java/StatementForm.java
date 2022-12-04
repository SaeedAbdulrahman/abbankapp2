import com.toedter.calendar.JMonthChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class StatementForm extends JFrame {
    private JPanel panel;
    private JPanel Controls;
    private JPanel DisplayPanel;
    private JRadioButton allAccountsRadioButton;
    private JRadioButton specificAccountRadioButton;
    private JButton allTimeButton;
    private JButton specificMonthButton;
    private JScrollBar scrollBar1;
    private JScrollPane statementPane;
    private JLabel criteria2;
    private JLabel criteria1;

    private String accountNumber = "";
    private String month = "";
    private boolean allTime = true;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        refreshCriteria();
    }

    public void setMonth(String month) {
        this.month = month;
        refreshCriteria();
    }

    public void setAllTime(boolean allTime) {
        this.allTime = allTime;
        refreshCriteria();
    }

    public void refreshCriteria() {
            criteria1.setText("Account: ".concat(accountNumber));
        if (allTime) {
            criteria2.setText("All time");
        } else {
            criteria2.setText("Month: ".concat(new DateFormatSymbols().getMonths()[Integer.parseInt(month)]));
        }
    }

    public StatementForm() {
        super("ABBank myStatement");
        String accountNumber = JOptionPane.showInputDialog(null, "Enter the account number that you want to view its statement! (Ex: 43211234567898)");
        this.setAccountNumber(accountNumber);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(970, 600);
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        APIHandle apiHandle = APIHandle.getInstance();
        showStatements(apiHandle.getStatement(accountNumber));
        allTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                    List<Map<String, String>> statements = apiHandle.getStatement(accountNumber);
                    showStatements(statements);
                }
        });
        specificMonthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<Map<String, String>> statements;
                JMonthChooser monthChooser = new JMonthChooser();
                String message = "Choose a specific month:\n";
                Object[] params = {message, monthChooser};
                JOptionPane.showConfirmDialog(null, params, "Choose month", JOptionPane.DEFAULT_OPTION);
                String month = String.valueOf(((JMonthChooser) params[1]).getMonth());
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
                setMonth(month);
                statements = apiHandle.getStatement(getAccountNumber());
                List<Map<String, String>> filteredStatements = new ArrayList<>();
                final int[] records = {0};
                statements.forEach(((log) -> {
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date date;
                                try {
                                    date = df.parse(log.get("logDate"));
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(date);
                                int logMonth = cal.get(Calendar.MONTH);
                                if (logMonth == Integer.parseInt(month)) {
                                    filteredStatements.add(log);
                                    records[0]++;
                                }
                }));
                if (records[0] == 0) {
                    JLabel entries = new JLabel("No statement entries found at the specified month!");
                    entries.setHorizontalAlignment(JLabel.CENTER);
                    statementPane.setViewportView(entries);
                    return;
                }
                setAllTime(false);
                showStatements(filteredStatements);
            }
        });
    }

    public void showStatements(List<Map<String, String>> statements) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        statements.forEach(((log) -> {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date date;
                    try {
                        date = df.parse(log.get("logDate"));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    StatementPanel statementPanel = new StatementPanel(cal.getTime().toString(), log.get("logType"), log.get("logMessage"));
                    panel.add(statementPanel.getPanel());
                })
        );
        statementPane.setViewportView(panel);
    }

}
