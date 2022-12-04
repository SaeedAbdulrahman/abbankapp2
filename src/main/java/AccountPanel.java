import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class AccountPanel extends JFrame {

    private JPanel panel;
    private JLabel Status;
    private JLabel Debt;
    private JLabel Balance;
    private JLabel accountNumber;
    private JLabel Type;

    public AccountPanel(String status, String debt, String balance, String number, String type) {
        Type.setText(type.toUpperCase());
        accountNumber.setText(number);
        Balance.setText(balance);
        Debt.setText(debt);
        Status.setText(status);
        if (status.equals("Active"))
            Status.setForeground(Color.decode("#4be02d"));
        else
            Status.setForeground(Color.decode("#de4a4a"));
    }


    public JPanel getPanel() {
        return panel;
    }

}
