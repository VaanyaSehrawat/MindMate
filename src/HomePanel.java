import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class HomePanel extends JFrame {

    public HomePanel() {
        setTitle("MindMate");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(18, 52, 110));
        add(root);

        JPanel left = buildLeftPanel();
        JPanel right = buildRightPanel();

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
    }

    private JPanel buildLeftPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(18, 52, 110));
        p.setPreferredSize(new Dimension(540, 800));
        p.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(10, 0, 10, 0);

        LogoIcon logo = new LogoIcon();
        logo.setPreferredSize(new Dimension(90, 90));
        g.gridy = 0; g.insets = new Insets(0, 0, 16, 0);
        p.add(logo, g);

        JLabel appName = new JLabel("MindMate");
        appName.setFont(new Font("SansSerif", Font.BOLD, 52));
        appName.setForeground(Color.WHITE);
        appName.setHorizontalAlignment(SwingConstants.LEFT);
        g.gridy = 1; g.insets = new Insets(0, 0, 8, 0);
        p.add(appName, g);

        JLabel tagline = new JLabel("Your safe space for mental wellness");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 18));
        tagline.setForeground(new Color(180, 210, 255));
        g.gridy = 2; g.insets = new Insets(0, 0, 40, 0);
        p.add(tagline, g);

        JPanel features = new JPanel(new GridLayout(3, 1, 0, 14));
        features.setBackground(new Color(18, 52, 110));
        features.add(featureItem("Track your mood daily"));
        features.add(featureItem("Talk to an AI support chatbot"));
        features.add(featureItem("Connect with a real therapist"));
        g.gridy = 3; g.insets = new Insets(0, 0, 50, 0);
        p.add(features, g);

        JButton getStarted = buildButton("Get Started", new Color(255, 255, 255), new Color(30, 90, 180));
        getStarted.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JButton login = buildButton("Login", new Color(30, 90, 180), Color.WHITE);
        login.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2, true));
        login.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        btnRow.setBackground(new Color(18, 52, 110));
        btnRow.add(getStarted);
        btnRow.add(login);
        g.gridy = 4; g.insets = new Insets(0, 0, 0, 0);
        p.add(btnRow, g);

        return p;
    }

    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(240, 245, 255));

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.insets = new Insets(12, 0, 12, 0);

        g.gridy = 0; p.add(infoCard("Mood Tracker",
            "Log your mood daily and track patterns over time.", new Color(30, 90, 180)), g);
        g.gridy = 1; p.add(infoCard("AI Chatbot",
            "Talk to our empathetic AI whenever you need support.", new Color(0, 150, 136)), g);
        g.gridy = 2; p.add(infoCard("Wellness Activities",
            "Breathing exercises, yoga poses and journaling prompts.", new Color(34, 139, 34)), g);

        return p;
    }

    private JPanel infoCard(String title, String desc, Color accent) {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(480, 90));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 225, 245), 1, true),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));

        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(5, 0));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 15));
        t.setForeground(new Color(20, 30, 60));

        JLabel d = new JLabel("<html><body style='width:340px'>" + desc + "</body></html>");
        d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        d.setForeground(new Color(90, 110, 150));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 4));
        text.setBackground(Color.WHITE);
        text.add(t);
        text.add(d);

        card.add(accentBar, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
        return card;
    }

    private JPanel featureItem(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        row.setBackground(new Color(18, 52, 110));

        JLabel dot = new JLabel("");
        dot.setOpaque(true);
        dot.setBackground(new Color(100, 180, 255));
        dot.setPreferredSize(new Dimension(8, 8));

        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        lbl.setForeground(new Color(200, 220, 255));

        row.add(dot);
        row.add(lbl);
        return row;
    }

    private JButton buildButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(fg);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static class LogoIcon extends JPanel {
        public LogoIcon() { setOpaque(false); }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int cx = getWidth() / 2, cy = getHeight() / 2, r = 38;
            g2.setColor(new Color(60, 120, 220));
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int[] hx = {cx - 16, cx - 16, cx, cx, cx + 16, cx + 16};
            int[] hy = {cy + 14, cy - 4, cy - 14, cy - 4, cy - 10, cy + 14};
            g2.drawPolyline(hx, hy, 6);
            g2.setColor(new Color(100, 180, 255));
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - r, cy - r, r * 2, r * 2);
        }
    }
}
