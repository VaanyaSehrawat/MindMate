import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class AdminFrame extends JFrame {
    private JPanel contentArea;
    private SidebarPanel sidebar;

    public AdminFrame(User admin) {
        setTitle("MindMate — Therapist Panel");
        setSize(1280, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try (Connection c = DatabaseHelper.connect(); Statement s = c.createStatement()) {
            s.execute("ALTER TABLE messages ADD COLUMN reply TEXT");
        } catch (SQLException ex) {}

        setLayout(new BorderLayout());
        sidebar = new SidebarPanel(admin.getUsername(), true);
        add(sidebar, BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(AppTheme.BG_MAIN);
        add(contentArea, BorderLayout.CENTER);

        showPanel("Overview");
        sidebar.setActive("Overview");
        sidebar.setSelectionListener(name -> showPanel(name));
    }

    private void showPanel(String name) {
        contentArea.removeAll();
        switch (name) {
            case "Overview":  contentArea.add(buildOverview(), BorderLayout.CENTER); break;
            case "Mood Logs": contentArea.add(buildMoodLogs(), BorderLayout.CENTER); break;
            case "Messages":  contentArea.add(buildMessages(), BorderLayout.CENTER); break;
            case "Analytics": contentArea.add(buildAnalytics(), BorderLayout.CENTER); break;
        }
        contentArea.revalidate();
        contentArea.repaint();
    }

    private JPanel buildOverview() {
        JPanel p = new JPanel(new BorderLayout(16,16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel title = new JLabel("Therapist Overview");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.NORTH);

        int[] stats = getStats();
        JPanel cards = new JPanel(new GridLayout(1,3,16,0));
        cards.setBackground(AppTheme.BG_MAIN);
        cards.add(statCard("Total Students", String.valueOf(stats[0]), AppTheme.PRIMARY));
        cards.add(statCard("Total Mood Logs", String.valueOf(stats[1]), AppTheme.ACCENT_TEAL));
        cards.add(statCard("Unread Messages", String.valueOf(stats[2]), AppTheme.ACCENT_RED));

        JPanel recent = AppTheme.card("Recent Mood Logs");
        recent.setLayout(new BorderLayout());
        String[] cols = {"Student","Date","Mood","Note"};
        DefaultTableModel m = new DefaultTableModel(cols,0);
        JTable t = new JTable(m); t.setRowHeight(26);
        t.setFont(new Font("SansSerif",Font.PLAIN,13));
        try (Connection c = DatabaseHelper.connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT u.username,m.log_date,m.mood,m.note FROM mood_logs m JOIN users u ON m.user_id=u.id WHERE u.role='user' ORDER BY m.id DESC LIMIT 10")) {
            while (rs.next()) m.addRow(new Object[]{rs.getString("username"),rs.getString("log_date"),rs.getString("mood"),rs.getString("note")});
        } catch (SQLException ex) { ex.printStackTrace(); }
        recent.add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0,16));
        center.setBackground(AppTheme.BG_MAIN);
        center.add(cards, BorderLayout.NORTH);
        center.add(recent, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMoodLogs() {
        JPanel p = new JPanel(new BorderLayout(16,16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        JLabel title = new JLabel("All Student Mood Logs");
        title.setFont(new Font("SansSerif",Font.BOLD,24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.NORTH);
        String[] cols = {"Student","Date","Mood","Note"};
        DefaultTableModel m = new DefaultTableModel(cols,0);
        JTable t = new JTable(m); t.setRowHeight(28); t.setFont(new Font("SansSerif",Font.PLAIN,14));
        try (Connection c = DatabaseHelper.connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT u.username,m.log_date,m.mood,m.note FROM mood_logs m JOIN users u ON m.user_id=u.id WHERE u.role='user' ORDER BY m.id DESC")) {
            while (rs.next()) m.addRow(new Object[]{rs.getString("username"),rs.getString("log_date"),rs.getString("mood"),rs.getString("note")});
        } catch (SQLException ex) { ex.printStackTrace(); }
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    private JPanel buildMessages() {
        JPanel p = new JPanel(new BorderLayout(16,16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24,24,24,24));
        JLabel title = new JLabel("Student Messages");
        title.setFont(new Font("SansSerif",Font.BOLD,24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.NORTH);

        String[] cols = {"#","Student","Date","Message","Reply"};
        DefaultTableModel m = new DefaultTableModel(cols,0) { public boolean isCellEditable(int r,int c){return false;} };
        JTable t = new JTable(m); t.setRowHeight(28); t.setFont(new Font("SansSerif",Font.PLAIN,13));
        t.getColumnModel().getColumn(3).setPreferredWidth(350);
        t.getColumnModel().getColumn(4).setPreferredWidth(250);
        int[] ids = loadMessages(m);

        JButton replyBtn = AppTheme.accentButton("Reply to Selected", AppTheme.ACCENT_GREEN);
        replyBtn.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(p,"Select a message first."); return; }
            showReplyDialog(ids[row],(String)m.getValueAt(row,3),m,row);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(AppTheme.BG_MAIN);
        bottom.add(replyBtn);

        p.add(new JScrollPane(t), BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildAnalytics() {
        JPanel p = new JPanel(new BorderLayout(16, 16));
        p.setBackground(AppTheme.BG_MAIN);
        p.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        JLabel title = new JLabel("Analytics Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.NORTH);

        Map<String, Integer> moodCounts = new LinkedHashMap<>();
        String[] moods = {"Happy", "Calm", "Anxious", "Sad", "Stressed", "Tired"};
        for (String mood : moods) moodCounts.put(mood, 0);
        int total = 0;
        try (Connection c = DatabaseHelper.connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT mood, COUNT(*) as cnt FROM mood_logs GROUP BY mood")) {
            while (rs.next()) {
                String mood = rs.getString("mood");
                int cnt = rs.getInt("cnt");
                if (moodCounts.containsKey(mood)) moodCounts.put(mood, cnt);
                total += cnt;
            }
        } catch (SQLException ex) { ex.printStackTrace(); }

        JPanel chartCard = AppTheme.card("Overall Mood Distribution (All Students)");
        chartCard.setLayout(new BorderLayout(16, 0));
        InsightsPanel.PieChart pie = new InsightsPanel.PieChart(moodCounts, total);
        chartCard.add(pie, BorderLayout.CENTER);
        chartCard.add(buildAdminLegend(moodCounts, total), BorderLayout.EAST);

        int[] stats = getStats();
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setBackground(AppTheme.BG_MAIN);
        statsRow.add(statCard("Students", String.valueOf(stats[0]), AppTheme.PRIMARY));
        statsRow.add(statCard("Total Logs", String.valueOf(total), AppTheme.ACCENT_TEAL));
        statsRow.add(statCard("Messages", String.valueOf(stats[2]), AppTheme.ACCENT_ORANGE));

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setBackground(AppTheme.BG_MAIN);
        center.add(statsRow, BorderLayout.NORTH);
        center.add(chartCard, BorderLayout.CENTER);
        p.add(center, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildAdminLegend(java.util.Map<String, Integer> data, int total) {
        Color[] colors = {
            new Color(255, 193, 7), new Color(0, 188, 212), new Color(255, 87, 34),
            new Color(156, 39, 176), new Color(244, 67, 54), new Color(96, 125, 139)
        };
        JPanel legend = new JPanel(new GridLayout(data.size(), 1, 0, 10));
        legend.setBackground(Color.WHITE);
        legend.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        int i = 0;
        for (java.util.Map.Entry<String, Integer> e : data.entrySet()) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            row.setBackground(Color.WHITE);
            JLabel dot = new JLabel("  ");
            dot.setOpaque(true);
            dot.setBackground(colors[i % colors.length]);
            dot.setPreferredSize(new Dimension(14, 14));
            int pct = total == 0 ? 0 : (int) Math.round((double) e.getValue() / total * 100);
            JLabel lbl = new JLabel(e.getKey() + " — " + e.getValue() + " (" + pct + "%)");
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
            lbl.setForeground(AppTheme.TEXT_PRIMARY);
            row.add(dot);
            row.add(lbl);
            legend.add(row);
            i++;
        }
        return legend;
    }
    private JPanel statCard(String label, String value, Color color) {
        JPanel card = new JPanel(new GridLayout(2,1,0,8));
        card.setBackground(Color.WHITE);
        card.setBorder(AppTheme.cardBorder());
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("SansSerif",Font.BOLD,32));
        val.setForeground(color);
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif",Font.PLAIN,13));
        lbl.setForeground(AppTheme.TEXT_SECONDARY);
        card.add(val); card.add(lbl);
        return card;
    }

    private int[] getStats() {
        int students=0, logs=0, msgs=0;
        try (Connection c = DatabaseHelper.connect(); Statement s = c.createStatement()) {
            ResultSet r1 = s.executeQuery("SELECT COUNT(*) FROM users WHERE role='user'"); if(r1.next()) students=r1.getInt(1);
            ResultSet r2 = s.executeQuery("SELECT COUNT(*) FROM mood_logs"); if(r2.next()) logs=r2.getInt(1);
            ResultSet r3 = s.executeQuery("SELECT COUNT(*) FROM messages WHERE reply IS NULL"); if(r3.next()) msgs=r3.getInt(1);
        } catch (SQLException ex) { ex.printStackTrace(); }
        return new int[]{students,logs,msgs};
    }

    private int[] loadMessages(DefaultTableModel m) {
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        try (Connection c = DatabaseHelper.connect();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT id,username,sent_date,message,reply FROM messages ORDER BY id DESC")) {
            while (rs.next()) {
                ids.add(rs.getInt("id"));
                m.addRow(new Object[]{ids.size(),rs.getString("username"),rs.getString("sent_date"),rs.getString("message"),rs.getString("reply")!=null?rs.getString("reply"):"Pending..."});
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return ids.stream().mapToInt(i->i).toArray();
    }

    private void showReplyDialog(int msgId, String origMsg, DefaultTableModel model, int row) {
        JDialog d = new JDialog(this,"Reply to Student",true);
        d.setSize(500,360); d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setBackground(new Color(245,255,245));
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        JLabel t = new JLabel("Reply to Student"); t.setFont(new Font("SansSerif",Font.BOLD,16)); t.setForeground(AppTheme.ACCENT_GREEN);
        p.add(t, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridLayout(2,1,0,10)); center.setBackground(new Color(245,255,245));
        JTextArea orig = new JTextArea(origMsg); orig.setEditable(false); orig.setLineWrap(true); orig.setWrapStyleWord(true); orig.setBackground(new Color(230,240,230));
        JTextArea reply = new JTextArea(); reply.setLineWrap(true); reply.setWrapStyleWord(true);
        JScrollPane s1 = new JScrollPane(orig); s1.setBorder(BorderFactory.createTitledBorder("Student's message"));
        JScrollPane s2 = new JScrollPane(reply); s2.setBorder(BorderFactory.createTitledBorder("Your reply"));
        center.add(s1); center.add(s2);
        p.add(center, BorderLayout.CENTER);
        JButton sendBtn = AppTheme.accentButton("Send Reply", AppTheme.ACCENT_GREEN);
        JLabel status = new JLabel(" "); status.setForeground(AppTheme.ACCENT_GREEN);
        JPanel bp = new JPanel(new FlowLayout(FlowLayout.RIGHT)); bp.setBackground(new Color(245,255,245));
        bp.add(status); bp.add(sendBtn);
        p.add(bp, BorderLayout.SOUTH);
        sendBtn.addActionListener(e -> {
            String r = reply.getText().trim();
            if (r.isEmpty()) { status.setForeground(AppTheme.ACCENT_RED); status.setText("Write a reply first."); return; }
            saveReply(msgId,r); model.setValueAt(r,row,4);
            status.setForeground(AppTheme.ACCENT_GREEN); status.setText("Reply sent!"); sendBtn.setEnabled(false);
        });
        d.add(p); d.setVisible(true);
    }

    private void saveReply(int id, String reply) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("UPDATE messages SET reply=? WHERE id=?")) {
            p.setString(1,reply); p.setInt(2,id); p.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }
}
