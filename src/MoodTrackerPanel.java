import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

public class MoodTrackerPanel extends JPanel {
    private User user;
    private DefaultTableModel model;
    private JTable table;
    private int[] entryIds = new int[0];
    private JLabel tipLabel;

    public MoodTrackerPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel title = new JLabel("Mood Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setBackground(AppTheme.BG_MAIN);

        JPanel logCard = AppTheme.card("Log Today's Mood");
        logCard.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        String[] moods = {"Happy", "Calm", "Anxious", "Sad", "Stressed", "Tired"};
        JComboBox<String> moodBox = new JComboBox<>(moods);
        moodBox.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JTextField noteField = new JTextField();
        noteField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tipLabel = new JLabel("<html><i>Select a mood and save.</i></html>");
        tipLabel.setForeground(AppTheme.PRIMARY);
        tipLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));

        g.gridx = 0; g.gridy = 0;
        logCard.add(new JLabel("Mood:"), g);
        g.gridy = 1; logCard.add(moodBox, g);
        g.gridy = 2; logCard.add(new JLabel("Note (optional):"), g);
        g.gridy = 3; logCard.add(noteField, g);
        g.gridy = 4;
        JButton saveBtn = AppTheme.primaryButton("Save Entry");
        logCard.add(saveBtn, g);
        g.gridy = 5; logCard.add(tipLabel, g);

        center.add(logCard);

        JPanel histCard = AppTheme.card("Mood History");
        histCard.setLayout(new BorderLayout(0, 8));
        String[] cols = {"Date", "Mood", "Note"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        table.getTableHeader().setBackground(AppTheme.BG_MAIN);
        histCard.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(Color.WHITE);
        JButton editBtn = AppTheme.accentButton("Edit", AppTheme.PRIMARY_LIGHT);
        JButton delBtn = AppTheme.accentButton("Delete", AppTheme.ACCENT_RED);
        editBtn.setPreferredSize(new Dimension(90, 32));
        delBtn.setPreferredSize(new Dimension(90, 32));
        btnRow.add(editBtn);
        btnRow.add(delBtn);
        histCard.add(btnRow, BorderLayout.SOUTH);
        center.add(histCard);

        add(center, BorderLayout.CENTER);
        loadHistory();

        saveBtn.addActionListener(e -> {
            String mood = (String) moodBox.getSelectedItem();
            String note = noteField.getText().trim();
            saveMood(mood, note);
            noteField.setText("");
            loadHistory();
            showMoodResponse(mood);
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select an entry first."); return; }
            if (JOptionPane.showConfirmDialog(this, "Delete this entry?", "Confirm", JOptionPane.YES_NO_OPTION) == 0) {
                deleteEntry(entryIds[row]);
                loadHistory();
            }
        });

        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select an entry first."); return; }
            showEditDialog(entryIds[row], (String) model.getValueAt(row, 1), (String) model.getValueAt(row, 2));
        });
    }

    private void showMoodResponse(String mood) {
        String[] data = getMoodData(mood);
        tipLabel.setText("<html><i>" + data[1] + "</i></html>");
        tipLabel.setForeground(new Color(Integer.parseInt(data[2])));
        JLabel msg = new JLabel("<html><body style='width:320px'>" + data[0] + "</body></html>");
        JOptionPane.showMessageDialog(this, msg, data[3], JOptionPane.INFORMATION_MESSAGE);
    }

    private String[] getMoodData(String mood) {
        switch (mood) {
            case "Happy": return new String[]{
                "<b>You are doing amazing!</b><br><br>- Share your happiness with others<br>- Write 3 things you are grateful for<br>- Do something kind today",
                "You are doing great! Keep spreading positivity.", "2289536", "Great going!"};
            case "Calm": return new String[]{
                "<b>What a wonderful state to be in!</b><br><br>- Great time to study or focus<br>- Try light meditation<br>- Journal your thoughts",
                "Perfect state of mind. Use it productively.", "32896", "You are at peace!"};
            case "Anxious": return new String[]{
                "<b>You are not alone in this.</b><br><br>- Box breathing: inhale 4s, hold 4s, exhale 4s<br>- Name 5 things you can see<br>- Step outside for fresh air",
                "Try box breathing: inhale 4s, hold 4s, exhale 4s.", "13395200", "It is okay to feel anxious."};
            case "Sad": return new String[]{
                "<b>Your feelings are valid.</b><br><br>- Talk to someone you trust<br>- Watch something that makes you laugh<br>- Go for a short walk",
                "Talk to someone you trust. You are not alone.", "6579392", "It is okay to feel sad."};
            case "Stressed": return new String[]{
                "<b>Stress means you care - do not let it overwhelm you.</b><br><br>- Write down everything on your mind<br>- Break tasks into smaller steps<br>- Take a 10 min break",
                "Break tasks into smaller steps. One thing at a time.", "11796544", "Let us tackle that stress!"};
            case "Tired": return new String[]{
                "<b>Your body and mind need a break.</b><br><br>- Take a 20 min power nap<br>- Drink water<br>- Sleep by 10pm tonight",
                "Rest is productive too. Take a short break.", "6579456", "Rest is important too."};
            default: return new String[]{"Keep tracking!", "Keep tracking your mood.", "1973100", "Saved!"};
        }
    }

    private void showEditDialog(int id, String curMood, String curNote) {
        JDialog d = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit Entry", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        d.setSize(380, 220); d.setLocationRelativeTo(this);
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6,6,6,6); g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        String[] moods = {"Happy","Calm","Anxious","Sad","Stressed","Tired"};
        JComboBox<String> mb = new JComboBox<>(moods); mb.setSelectedItem(curMood);
        JTextField nf = new JTextField(curNote);
        JButton sv = AppTheme.primaryButton("Save Changes");
        g.gridy=0; p.add(new JLabel("Mood:"),g);
        g.gridy=1; p.add(mb,g);
        g.gridy=2; p.add(new JLabel("Note:"),g);
        g.gridy=3; p.add(nf,g);
        g.gridy=4; p.add(sv,g);
        sv.addActionListener(e -> { updateEntry(id,(String)mb.getSelectedItem(),nf.getText().trim()); d.dispose(); loadHistory(); });
        d.add(p); d.setVisible(true);
    }

    private void saveMood(String mood, String note) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("INSERT INTO mood_logs (user_id,mood,note,log_date) VALUES (?,?,?,?)")) {
            p.setInt(1,user.getId()); p.setString(2,mood); p.setString(3,note); p.setString(4,LocalDate.now().toString());
            p.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void updateEntry(int id, String mood, String note) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("UPDATE mood_logs SET mood=?,note=? WHERE id=?")) {
            p.setString(1,mood); p.setString(2,note); p.setInt(3,id); p.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void deleteEntry(int id) {
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("DELETE FROM mood_logs WHERE id=?")) {
            p.setInt(1,id); p.executeUpdate();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void loadHistory() {
        model.setRowCount(0);
        java.util.List<Integer> ids = new java.util.ArrayList<>();
        try (Connection c = DatabaseHelper.connect();
             PreparedStatement p = c.prepareStatement("SELECT id,log_date,mood,note FROM mood_logs WHERE user_id=? ORDER BY id DESC")) {
            p.setInt(1,user.getId());
            ResultSet rs = p.executeQuery();
            while (rs.next()) { ids.add(rs.getInt("id")); model.addRow(new Object[]{rs.getString("log_date"),rs.getString("mood"),rs.getString("note")}); }
        } catch (SQLException ex) { ex.printStackTrace(); }
        entryIds = ids.stream().mapToInt(i->i).toArray();
    }
}
