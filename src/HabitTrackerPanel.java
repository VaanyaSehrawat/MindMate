import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class HabitTrackerPanel extends JPanel {
    public HabitTrackerPanel() {
        setLayout(new BorderLayout(16,16));
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel title = new JLabel("Daily Habit Tracker");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel date = new JLabel("Today: " + LocalDate.now());
        date.setFont(new Font("SansSerif", Font.PLAIN, 14));
        date.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel header = new JPanel(new GridLayout(2,1,0,4));
        header.setBackground(AppTheme.BG_MAIN);
        header.add(title); header.add(date);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(3, 3, 16, 16));
        grid.setBackground(AppTheme.BG_MAIN);

        String[][] habits = {
            {"Drink 8 glasses of water", "Hydration"},
            {"30 mins of exercise", "Physical"},
            {"8 hours of sleep", "Rest"},
            {"Read for 15 mins", "Mental"},
            {"Meditate 5 mins", "Mindfulness"},
            {"No social media before 9am", "Digital Wellness"},
            {"Eat a healthy meal", "Nutrition"},
            {"Call or text a friend", "Social"},
            {"Journal your thoughts", "Reflection"}
        };

        for (String[] habit : habits) {
            grid.add(buildHabitCard(habit[0], habit[1]));
        }

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(AppTheme.BG_MAIN);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel buildHabitCard(String habit, String category) {
        JPanel card = new JPanel(new BorderLayout(8,8));
        card.setBackground(Color.WHITE);
        card.setBorder(AppTheme.cardBorder());

        JLabel cat = new JLabel(category);
        cat.setFont(new Font("SansSerif", Font.BOLD, 11));
        cat.setForeground(AppTheme.PRIMARY_LIGHT);

        JLabel name = new JLabel("<html><body style='width:130px'>" + habit + "</body></html>");
        name.setFont(new Font("SansSerif", Font.PLAIN, 13));
        name.setForeground(AppTheme.TEXT_PRIMARY);

        JPanel text = new JPanel(new GridLayout(2,1,0,4));
        text.setBackground(Color.WHITE);
        text.add(cat); text.add(name);

        JCheckBox check = new JCheckBox("Done");
        check.setFont(new Font("SansSerif", Font.BOLD, 13));
        check.setBackground(Color.WHITE);
        check.setForeground(AppTheme.TEXT_SECONDARY);
        check.addActionListener(e -> {
            if (check.isSelected()) {
                check.setForeground(AppTheme.ACCENT_GREEN);
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.ACCENT_GREEN, 2, true),
                    BorderFactory.createEmptyBorder(14,14,14,14)
                ));
            } else {
                check.setForeground(AppTheme.TEXT_SECONDARY);
                card.setBorder(AppTheme.cardBorder());
            }
        });

        card.add(text, BorderLayout.CENTER);
        card.add(check, BorderLayout.SOUTH);
        return card;
    }
}
