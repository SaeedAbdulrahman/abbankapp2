import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class LoginForm extends JFrame {
    private JPanel panel;
    private JButton loginButton;
    private JTextField usernameField;
    private JButton resetPasswordButton;
    private JPasswordField passwordField;

    public LoginForm() {
        super("ABBank Banker Login");
        ImageIcon backgroundImage = new ImageIcon(Objects.requireNonNull(getImage("login_background.png")));
        this.setContentPane(new JLabel(backgroundImage));
        this.setLayout(new GridBagLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(995, 588);
        panel.setOpaque(false);
        this.add(panel);
        this.setResizable(false);
        this.setVisible(true);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                if (username.equals("") || password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Please enter your username and password!", "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    APIHandle apiHandle = APIHandle.getInstance();
                    if (apiHandle.signin(username, password)) {
                        JOptionPane.showMessageDialog(null, "Successfully Logged In!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                        transition();
                    } else {
                        JOptionPane.showMessageDialog(null, "Wrong username or password!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        new LoginForm();
//        new EditProfile();
    }

    private void transition() {
        this.setVisible(false);
        new CustomerForm();
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
