import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class UserFrame extends JFrame {
    private User user;
    private JPanel contentArea;
    private SidebarPanel sidebar;

    public UserFrame(User user) {
        this.user = user;
        setTitle("MindMate");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        sidebar = new SidebarPanel(user.getUsername(), false);
        add(sidebar, BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(AppTheme.BG_MAIN);
        add(contentArea, BorderLayout.CENTER);

        showPanel("Dashboard");
        sidebar.setActive("Dashboard");
        sidebar.setSelectionListener(name -> showPanel(name));
    }

    private void showPanel(String name) {
        contentArea.removeAll();
        switch (name) {
            case "Dashboard":    contentArea.add(buildDashboard(), BorderLayout.CENTER); break;
            case "Mood Tracker": contentArea.add(new MoodTrackerPanel(user), BorderLayout.CENTER); break;
            case "AI Chatbot":   contentArea.add(new ChatbotPanel(user), BorderLayout.CENTER); break;
            case "Activities":   contentArea.add(new ActivitiesPanel(), BorderLayout.CENTER); break;
            case "Habit Tracker":contentArea.add(new HabitTrackerPanel(), BorderLayout.CENTER); break;
            case "Insights":     contentArea.add(new InsightsPanel(user), BorderLayout.CENTER); break;
            case "Therapist":    contentArea.add(buildTherapistPanel(), BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    private JPanel buildDashboard() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel topBanner = new JPanel(new BorderLayout());
        topBanner.setBackground(AppTheme.PRIMARY);
        topBanner.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JLabel logo = new JLabel("MindMate");
        logo.setFont(new Font("SansSerif", Font.BOLD, 32));
        logo.setForeground(Color.WHITE);

        JLabel tagline = new JLabel("Your Mental Wellness Partner");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 15));
        tagline.setForeground(new Color(190, 215, 255));

        JLabel welcome = new JLabel("Welcome back, " + user.getUsername() + "!");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 18));
        welcome.setForeground(Color.WHITE);

        JPanel bannerLeft = new JPanel(new GridLayout(2, 1, 0, 6));
        bannerLeft.setBackground(AppTheme.PRIMARY);
        bannerLeft.add(logo);
        bannerLeft.add(tagline);

        topBanner.add(bannerLeft, BorderLayout.WEST);
        topBanner.add(welcome, BorderLayout.EAST);
        p.add(topBanner, BorderLayout.NORTH);

        JLabel sectionLabel = new JLabel("What would you like to do today?");
        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        sectionLabel.setForeground(AppTheme.TEXT_SECONDARY);
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(16, 0, 8, 0));

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setBackground(AppTheme.BG_MAIN);

        grid.add(quickCard("Mood Tracker",    "Log how you feel today",        AppTheme.PRIMARY,        "Mood Tracker"));
        grid.add(quickCard("AI Chatbot",      "Talk to your support bot",      AppTheme.ACCENT_TEAL,    "AI Chatbot"));
        grid.add(quickCard("Activities",      "Breathing, yoga and more",      AppTheme.ACCENT_GREEN,   "Activities"));
        grid.add(quickCard("Habit Tracker",   "Track your daily goals",        AppTheme.ACCENT_ORANGE,  "Habit Tracker"));
        grid.add(quickCard("Insights",        "View your mood patterns",       AppTheme.PRIMARY_LIGHT,  "Insights"));
        grid.add(quickCard("Therapist",       "Message your therapist",        new Color(120, 60, 180), "Therapist"));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BG_MAIN);
        center.add(sectionLabel, BorderLayout.NORTH);
        center.add(grid, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel quickCard(String title, String desc, Color color, String target) {
        JPanel card = new JPanel(new BorderLayout(8, 8)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
            }
        };
        card.setBackground(color);
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 16, 20));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 17));
        t.setForeground(Color.WHITE);

        JLabel d = new JLabel("<html>" + desc + "</html>");
        d.setFont(new Font("SansSerif", Font.PLAIN, 13));
        d.setForeground(new Color(220, 235, 255));

        JButton btn = new JButton("Open");
        btn.setBackground(new Color(255, 255, 255, 50));
        btn.setForeground(Color.WHITE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(t, BorderLayout.NORTH);
        card.add(d, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);

        MouseAdapter hover = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { sidebar.setActive(target); showPanel(target); }
            public void mouseEntered(MouseEvent e) { card.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e)  { card.setBackground(color); }
        };
        card.addMouseListener(hover);
        btn.addActionListener(e -> { sidebar.setActive(target); showPanel(target); });
        return card;
    }

    private JPanel buildTherapistPanel() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Therapist Support");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setBackground(AppTheme.BG_MAIN);

        JPanel sendCard = AppTheme.card("Send a Message");
        sendCard.setLayout(new BorderLayout(8, 8));
        JTextArea msgArea = new JTextArea();
        msgArea.setLineWrap(true);
        msgArea.setWrapStyleWord(true);
        msgArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msgArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JScrollPane ms = new JScrollPane(msgArea);
        ms.setBorder(BorderFactory.createLineBorder(new Color(200, 215, 240), 1, true));
        JButton sendBtn = AppTheme.primaryButton("Send to Therapist");
        JLabel status = new JLabel(" ");
        status.setForeground(AppTheme.ACCENT_GREEN);
        JPanel sb = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sb.setBackground(Color.WHITE);
        sb.add(status);
        sb.add(sendBtn);
        sendCard.add(ms, BorderLayout.CENTER);
        sendCard.add(sb, BorderLayout.SOUTH);
        sendBtn.addActionListener(e -> {
            String msg = msgArea.getText().trim();
            if (msg.isEmpty()) { status.setForeground(AppTheme.ACCENT_RED); status.setText("Write a message first."); return; }
            saveMessage(msg);
            status.setForeground(AppTheme.ACCENT_GREEN);
            status.setText("Message sent!");
            msgArea.setText("");
            sendBtn.setEnabled(false);
        });

        JPanel repliesCard = AppTheme.card("Therapist Replies");
        repliesCard.setLayout(new BorderLayout());
        String[] cols = {"Date", "Your Message", "Reply"};
        javax.swing.table.DefaultTableModel rm = new javax.swing.table.DefaultTableModel(cols, 0);
        JTable rt = new JTable(rm);
        rt.setRowHeight(28);
        rt.setFont(new Font("SansSerif", Font.PLAIN, 13));
        rt.getColumnModel().getColumn(1).setPreferredWidth(200);
        rt.getColumnModel().getColumn(2).setPreferredWidth(250);
        loadReplies(rm);
        repliesCard.add(new JScrollPane(rt), BorderLayout.CENTER);

        center.add(sendCard);
        center.add(repliesCard);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private void saveMessage(String msg) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("INSERT INTO messages (user_id,username,message,sent_date) VALUES (?,?,?,?)")) {
            p.setInt(1, user.getId());
            p.setString(2, user.getUsername());
            p.setString(3, msg);
            p.setString(4, LocalDate.now().toString());
            p.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadReplies(javax.swing.table.DefaultTableModel m) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("SELECT sent_date,message,reply FROM messages WHERE user_id=? ORDER BY id DESC")) {
            p.setInt(1, user.getId());
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                m.addRow(new Object[]{
                    rs.getString("sent_date"),
                    rs.getString("message"),
                    rs.getString("reply") != null ? rs.getString("reply") : "Awaiting reply..."
                });
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
