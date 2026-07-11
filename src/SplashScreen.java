import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JFrame {

    public SplashScreen() {
        setTitle("MindMate");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(108, 99, 255));
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.anchor = GridBagConstraints.CENTER;

        JLabel emoji = new JLabel("🧠");
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 72));
        g.gridy = 0; g.insets = new Insets(0, 20, 10, 20);
        main.add(emoji, g);

        JLabel title = new JLabel("MindMate");
        title.setFont(new Font("SansSerif", Font.BOLD, 56));
        title.setForeground(Color.WHITE);
        g.gridy = 1; g.insets = new Insets(0, 20, 8, 20);
        main.add(title, g);

        JLabel tagline = new JLabel("Your Mental Wellness Partner On Campus");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 20));
        tagline.setForeground(new Color(220, 215, 255));
        g.gridy = 2; g.insets = new Insets(0, 20, 30, 20);
        main.add(tagline, g);

        JPanel features = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        features.setOpaque(false);
        features.add(makeFeatureLabel("Track your mood daily"));
        features.add(makeFeatureLabel("Get wellness tips"));
        features.add(makeFeatureLabel("Connect with a therapist"));
        g.gridy = 3; g.insets = new Insets(0, 20, 40, 20);
        main.add(features, g);

        JButton startBtn = new JButton("Get Started");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        startBtn.setForeground(new Color(108, 99, 255));
        startBtn.setBackground(Color.WHITE);
        startBtn.setOpaque(true);
        startBtn.setBorderPainted(false);
        startBtn.setFocusPainted(false);
        startBtn.setPreferredSize(new Dimension(220, 52));
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        g.gridy = 4; g.insets = new Insets(0, 20, 20, 20);
        main.add(startBtn, g);

        JLabel footer = new JLabel("Free  •  Confidential  •  Always here for you");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 13));
        footer.setForeground(new Color(200, 195, 255));
        g.gridy = 5; g.insets = new Insets(10, 20, 0, 20);
        main.add(footer, g);

        add(main);
    }

    private JLabel makeFeatureLabel(String text) {
        JLabel lbl = new JLabel("✓  " + text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lbl.setForeground(new Color(220, 215, 255));
        return lbl;
    }
}
