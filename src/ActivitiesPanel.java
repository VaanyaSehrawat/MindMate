import java.time.LocalDate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ActivitiesPanel extends JPanel {
    private Timer breathTimer;
    private int breathPhase = 0;
    private int breathCount = 0;
    private JLabel breathLabel;
    private JButton breathBtn;

    public ActivitiesPanel() {
        setLayout(new BorderLayout(16,16));
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel title = new JLabel("Wellness Activities");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 2, 16, 16));
        grid.setBackground(AppTheme.BG_MAIN);

        grid.add(buildBreathingCard());
        grid.add(buildJournalCard());
        grid.add(buildYogaCard());
        grid.add(buildAffirmationCard());

        add(grid, BorderLayout.CENTER);
    }

    private JPanel buildBreathingCard() {
        JPanel card = AppTheme.card("Box Breathing Exercise");
        card.setLayout(new BorderLayout(8,8));

        breathLabel = new JLabel("<html><center>Press Start to begin<br>guided breathing</center></html>", SwingConstants.CENTER);
        breathLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        breathLabel.setForeground(AppTheme.PRIMARY);

        breathBtn = AppTheme.primaryButton("Start");
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(breathBtn);

        card.add(breathLabel, BorderLayout.CENTER);
        card.add(btnRow, BorderLayout.SOUTH);

        String[] phases = {"Inhale... (4s)", "Hold... (4s)", "Exhale... (4s)", "Hold... (4s)"};
        Color[] colors = {AppTheme.ACCENT_TEAL, AppTheme.PRIMARY, AppTheme.ACCENT_GREEN, AppTheme.PRIMARY_DARK};

        breathTimer = new Timer(4000, null);
        breathTimer.addActionListener(e -> {
            breathPhase = (breathPhase + 1) % 4;
            if (breathPhase == 0) breathCount++;
            breathLabel.setText("<html><center>" + phases[breathPhase] + "<br><small>Round " + (breathCount+1) + "</small></center></html>");
            breathLabel.setForeground(colors[breathPhase]);
            if (breathCount >= 4) {
                breathTimer.stop();
                breathPhase = 0; breathCount = 0;
                breathLabel.setText("<html><center>Well done!<br>You completed 4 rounds.</center></html>");
                breathLabel.setForeground(AppTheme.ACCENT_GREEN);
                breathBtn.setText("Start Again");
            }
        });

        breathBtn.addActionListener(e -> {
            breathPhase = 0; breathCount = 0;
            breathLabel.setText("<html><center>" + phases[0] + "<br><small>Round 1</small></center></html>");
            breathLabel.setForeground(colors[0]);
            breathBtn.setText("Running...");
            breathBtn.setEnabled(false);
            breathTimer.start();
            Timer enable = new Timer(16500, ev -> breathBtn.setEnabled(true));
            enable.setRepeats(false); enable.start();
        });

        return card;
    }

    private JPanel buildJournalCard() {
        JPanel card = AppTheme.card("Journaling Prompts");
        card.setLayout(new BorderLayout(8,8));

        String[] prompts = {
            "What made you smile today?",
            "What is one thing you are grateful for?",
            "What is one challenge you overcame recently?",
            "What would make tomorrow better?",
            "What are 3 things you love about yourself?",
            "What is something you are looking forward to?"
        };

        JLabel promptLabel = new JLabel("<html><body style='width:200px'>" + prompts[0] + "</body></html>");
        promptLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        promptLabel.setForeground(AppTheme.TEXT_PRIMARY);
        promptLabel.setBorder(BorderFactory.createEmptyBorder(8,0,8,0));

        JButton nextBtn = AppTheme.primaryButton("New Prompt");
        int[] idx = {0};
        nextBtn.addActionListener(e -> {
            idx[0] = (idx[0] + 1) % prompts.length;
            promptLabel.setText("<html><body style='width:200px'>" + prompts[idx[0]] + "</body></html>");
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(nextBtn);

        card.add(promptLabel, BorderLayout.CENTER);
        card.add(btnRow, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildYogaCard() {
        JPanel card = AppTheme.card("Quick Yoga Poses");
        card.setLayout(new BorderLayout(8,8));

        String[][] poses = {
            {"Child's Pose", "Kneel, sit back on heels, stretch arms forward, hold 30s"},
            {"Cat-Cow Stretch", "On all fours, arch and round your back, repeat 10x"},
            {"Standing Forward Fold", "Stand, bend forward, let arms hang, hold 30s"},
            {"Seated Twist", "Sit cross-legged, twist right then left, hold 20s each"},
            {"Legs Up The Wall", "Lie on back, legs straight up wall, hold 2 mins"}
        };

        JLabel poseTitle = new JLabel(poses[0][0]);
        poseTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        poseTitle.setForeground(AppTheme.PRIMARY);

        JLabel poseDesc = new JLabel("<html><body style='width:180px'>" + poses[0][1] + "</body></html>");
        poseDesc.setFont(new Font("SansSerif", Font.PLAIN, 13));
        poseDesc.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel textPanel = new JPanel(new GridLayout(2,1,0,6));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(poseTitle);
        textPanel.add(poseDesc);

        int[] idx = {0};
        JButton nextBtn = AppTheme.primaryButton("Next Pose");
        nextBtn.addActionListener(e -> {
            idx[0] = (idx[0]+1) % poses.length;
            poseTitle.setText(poses[idx[0]][0]);
            poseDesc.setText("<html><body style='width:180px'>" + poses[idx[0]][1] + "</body></html>");
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnRow.setBackground(Color.WHITE);
        btnRow.add(nextBtn);

        card.add(textPanel, BorderLayout.CENTER);
        card.add(btnRow, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildAffirmationCard() {
        JPanel card = AppTheme.card("Daily Affirmation");
        card.setLayout(new BorderLayout(8,8));

        String[] affirmations = {
            "I am capable of handling whatever comes my way.",
            "I deserve peace, happiness, and good things.",
            "I am growing and learning every single day.",
            "My feelings are valid and I am allowed to feel them.",
            "I am enough exactly as I am.",
            "I choose to be kind to myself today.",
            "I am proud of how far I have come."
        };

        int day = LocalDate.now().getDayOfYear() % affirmations.length;
        JLabel affLabel = new JLabel("<html><body style='width:200px;text-align:center'><i>\"" + affirmations[day] + "\"</i></body></html>", SwingConstants.CENTER);
        affLabel.setFont(new Font("SansSerif", Font.ITALIC, 15));
        affLabel.setForeground(AppTheme.PRIMARY);

        card.add(affLabel, BorderLayout.CENTER);
        return card;
    }
}
