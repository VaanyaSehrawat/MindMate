import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class InsightsPanel extends JPanel {
    public InsightsPanel(User user) {
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Mood Insights");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        Map<String, Integer> moodCounts = new LinkedHashMap<>();
        String[] moods = {"Happy", "Calm", "Anxious", "Sad", "Stressed", "Tired"};
        for (String m : moods) moodCounts.put(m, 0);
        int total = 0;
        String mostCommon = "None";
        int max = 0;

        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("SELECT mood, COUNT(*) as cnt FROM mood_logs WHERE user_id=? GROUP BY mood")) {
            p.setInt(1, user.getId());
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String m = rs.getString("mood");
                int cnt = rs.getInt("cnt");
                if (moodCounts.containsKey(m)) moodCounts.put(m, cnt);
                total += cnt;
                if (cnt > max) { max = cnt; mostCommon = m; }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }

        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setBackground(AppTheme.BG_MAIN);
        statsRow.add(statCard("Total Entries", String.valueOf(total), AppTheme.PRIMARY));
        statsRow.add(statCard("Most Common Mood", mostCommon, AppTheme.ACCENT_TEAL));
        long tracked = moodCounts.values().stream().filter(v -> v > 0).count();
        statsRow.add(statCard("Moods Tracked", String.valueOf(tracked), AppTheme.ACCENT_GREEN));

        JPanel chartCard = AppTheme.card("Mood Distribution");
        chartCard.setLayout(new BorderLayout(16, 0));
        PieChart pie = new PieChart(moodCounts, total);
        chartCard.add(pie, BorderLayout.CENTER);
        chartCard.add(buildLegend(moodCounts, total), BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setBackground(AppTheme.BG_MAIN);
        center.add(statsRow, BorderLayout.NORTH);
        center.add(chartCard, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildLegend(Map<String, Integer> data, int total) {
        Color[] colors = {
            new Color(255, 193, 7), new Color(0, 188, 212), new Color(255, 87, 34),
            new Color(156, 39, 176), new Color(244, 67, 54), new Color(96, 125, 139)
        };
        JPanel legend = new JPanel(new GridLayout(data.size(), 1, 0, 10));
        legend.setBackground(Color.WHITE);
        legend.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        int i = 0;
        for (Map.Entry<String, Integer> e : data.entrySet()) {
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
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(AppTheme.cardBorder());
        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("SansSerif", Font.BOLD, 28));
        val.setForeground(color);
        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lbl.setForeground(AppTheme.TEXT_SECONDARY);
        card.add(val);
        card.add(lbl);
        return card;
    }

    static class PieChart extends JPanel {
        Map<String, Integer> data;
        int total;
        Color[] colors = {
            new Color(255, 193, 7), new Color(0, 188, 212), new Color(255, 87, 34),
            new Color(156, 39, 176), new Color(244, 67, 54), new Color(96, 125, 139)
        };

        PieChart(Map<String, Integer> data, int total) {
            this.data = data;
            this.total = total;
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(300, 300));
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (total == 0) {
                g2.setColor(AppTheme.TEXT_SECONDARY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString("No data yet", getWidth() / 2 - 40, getHeight() / 2);
                return;
            }

            int size = Math.min(getWidth(), getHeight()) - 40;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;
            int startAngle = 0;
            int i = 0;

            for (Map.Entry<String, Integer> e : data.entrySet()) {
                if (e.getValue() == 0) { i++; continue; }
                int arc = (int) Math.round((double) e.getValue() / total * 360);
                if (i == data.size() - 1) arc = 360 - startAngle;
                g2.setColor(colors[i % colors.length]);
                g2.fillArc(x, y, size, size, startAngle, arc);
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2));
                g2.drawArc(x, y, size, size, startAngle, arc);

                double midAngle = Math.toRadians(startAngle + arc / 2.0);
                int pct = (int) Math.round((double) e.getValue() / total * 100);
                if (pct >= 5) {
                    int lx = (int) (x + size / 2 + (size / 2.8) * Math.cos(midAngle));
                    int ly = (int) (y + size / 2 - (size / 2.8) * Math.sin(midAngle));
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                    g2.drawString(pct + "%", lx - 12, ly + 5);
                }
                startAngle += arc;
                i++;
            }

            int hole = size / 3;
            g2.setColor(Color.WHITE);
            g2.fillOval(x + size / 2 - hole / 2, y + size / 2 - hole / 2, hole, hole);
            g2.setColor(AppTheme.TEXT_PRIMARY);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            String totalStr = String.valueOf(total);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(totalStr, x + size / 2 - fm.stringWidth(totalStr) / 2, y + size / 2 + 5);
            g2.setColor(AppTheme.TEXT_SECONDARY);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
            g2.drawString("total", x + size / 2 - 12, y + size / 2 + 20);
        }
    }
}