import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("MindMate — Login");
        setSize(480, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG_MAIN);

        JPanel top = new JPanel(new GridLayout(2, 1, 0, 4)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, AppTheme.PRIMARY_DARK, getWidth(), getHeight(), AppTheme.PRIMARY_LIGHT);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        top.setPreferredSize(new Dimension(0, 110));
        top.setBorder(BorderFactory.createEmptyBorder(24, 32, 20, 32));
        top.setOpaque(false);

        JLabel logo = new JLabel("MindMate");
        logo.setFont(new Font("SansSerif", Font.BOLD, 30));
        logo.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Your Mental Wellness Partner");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tagline.setForeground(new Color(200, 220, 255));

        top.add(logo);
        top.add(tagline);
        root.add(top, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.BG_MAIN);
        form.setBorder(BorderFactory.createEmptyBorder(28, 40, 20, 40));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 0, 6, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JTextField userField = new JTextField();
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JCheckBox showPass = new JCheckBox("Show password");
        showPass.setBackground(AppTheme.BG_MAIN);
        showPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPass.setForeground(AppTheme.TEXT_SECONDARY);
        showPass.addActionListener(e ->
            passField.setEchoChar(showPass.isSelected() ? (char) 0 : '•'));

        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        userLbl.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        passLbl.setForeground(AppTheme.TEXT_PRIMARY);

        JButton loginBtn = AppTheme.primaryButton("Log In");
        loginBtn.setPreferredSize(new Dimension(0, 42));

        JButton signupBtn = AppTheme.accentButton("Create Account", AppTheme.ACCENT_GREEN);
        signupBtn.setPreferredSize(new Dimension(0, 42));

        JLabel msg = new JLabel(" ", SwingConstants.CENTER);
        msg.setForeground(AppTheme.ACCENT_RED);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));

        g.gridy = 0; form.add(userLbl, g);
        g.gridy = 1; form.add(userField, g);
        g.gridy = 2; form.add(passLbl, g);
        g.gridy = 3; form.add(passField, g);
        g.gridy = 4; form.add(showPass, g);
        g.gridy = 5; g.insets = new Insets(10, 0, 6, 0); form.add(loginBtn, g);
        g.gridy = 6; g.insets = new Insets(4, 0, 6, 0); form.add(signupBtn, g);
        g.gridy = 7; form.add(msg, g);

        root.add(form, BorderLayout.CENTER);
        add(root);

        loginBtn.addActionListener(e -> {
            String uname = userField.getText().trim();
            String pass = new String(passField.getPassword()).trim();
            if (uname.isEmpty() || pass.isEmpty()) {
                msg.setText("Please enter username and password.");
                return;
            }
            User user = authenticate(uname, pass);
            if (user != null) {
                dispose();
                if (user.isAdmin()) new AdminFrame(user).setVisible(true);
                else new UserFrame(user).setVisible(true);
            } else {
                msg.setText("Invalid username or password.");
                passField.setText("");
            }
        });

        signupBtn.addActionListener(e -> showSignupDialog(msg));

        getRootPane().setDefaultButton(loginBtn);
    }

    private void showSignupDialog(JLabel msg) {
        JDialog dialog = new JDialog(this, "Create Account", true);
        dialog.setSize(400, 360);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppTheme.BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 0, 6, 0);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(AppTheme.PRIMARY);

        JTextField newUser = new JTextField();
        newUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newUser.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPasswordField newPass = new JPasswordField();
        newPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        newPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JPasswordField confirmPass = new JPasswordField();
        confirmPass.setFont(new Font("SansSerif", Font.PLAIN, 14));
        confirmPass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JCheckBox showSignupPass = new JCheckBox("Show passwords");
        showSignupPass.setBackground(AppTheme.BG_MAIN);
        showSignupPass.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showSignupPass.addActionListener(e -> {
            char echo = showSignupPass.isSelected() ? (char) 0 : '•';
            newPass.setEchoChar(echo);
            confirmPass.setEchoChar(echo);
        });

        JButton createBtn = AppTheme.primaryButton("Create Account");
        createBtn.setPreferredSize(new Dimension(0, 42));

        JLabel errLabel = new JLabel(" ", SwingConstants.CENTER);
        errLabel.setForeground(AppTheme.ACCENT_RED);
        errLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        g.gridy = 0; panel.add(title, g);
        g.gridy = 1; panel.add(new fieldLabel("Username"), g);
        g.gridy = 2; panel.add(newUser, g);
        g.gridy = 3; panel.add(new fieldLabel("Password"), g);
        g.gridy = 4; panel.add(newPass, g);
        g.gridy = 5; panel.add(new fieldLabel("Confirm Password"), g);
        g.gridy = 6; panel.add(confirmPass, g);
        g.gridy = 7; panel.add(showSignupPass, g);
        g.gridy = 8; g.insets = new Insets(10, 0, 4, 0); panel.add(createBtn, g);
        g.gridy = 9; g.insets = new Insets(4, 0, 0, 0); panel.add(errLabel, g);

        createBtn.addActionListener(e -> {
            String username = newUser.getText().trim();
            String password = new String(newPass.getPassword()).trim();
            String confirm = new String(confirmPass.getPassword()).trim();
            if (username.isEmpty() || password.isEmpty()) {
                errLabel.setText("Please fill in all fields."); return;
            }
            if (password.length() < 4) {
                errLabel.setText("Password must be at least 4 characters."); return;
            }
            if (!password.equals(confirm)) {
                errLabel.setText("Passwords do not match."); return;
            }
            if (registerUser(username, password)) {
                dialog.dispose();
                msg.setForeground(AppTheme.ACCENT_GREEN);
                msg.setText("Account created! You can now log in.");
            } else {
                errLabel.setText("Username already taken.");
            }
        });

        dialog.add(panel);
        dialog.setVisible(true);
    }

    static class fieldLabel extends JLabel {
        fieldLabel(String text) {
            super(text);
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(AppTheme.TEXT_PRIMARY);
        }
    }

    private boolean registerUser(String username, String password) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pst = conn.prepareStatement(
                 "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')")) {
            pst.setString(1, username);
            pst.setString(2, password);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) { return false; }
    }

    private User authenticate(String username, String password) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement pst = conn.prepareStatement(
                 "SELECT id, username, role FROM users WHERE username=? AND password=?")) {
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next())
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("role"));
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }
}
