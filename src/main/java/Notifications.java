import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class Notifications {
    private JPanel panel;
    private JLabel sDate;
    private JLabel sType;
    private JLabel sBody;
    private JPanel holder;

    public Notifications(String date, String type, String body) {
        holder.setBackground(Color.decode("#de4a4a"));
        sType.setText(type.toUpperCase());
        sDate.setText(date);
        sBody.setText(body);
    }

    public JPanel getPanel() {
        return panel;
    }

}
