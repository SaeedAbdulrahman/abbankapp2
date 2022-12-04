import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class StatementPanel {
    private JLabel sDate;
    private JLabel sType;
    private JLabel sBody;
    private JPanel panel;

    public StatementPanel(String date, String type, String body) {
        sType.setText(type.toUpperCase());
        sDate.setText(date);
        sBody.setText(body);
    }

    public JPanel getPanel() {
        return panel;
    }

}
