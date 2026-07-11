import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class ChatbotPanel extends JPanel {
    private JPanel chatArea;
    private JTextField inputField;
    private JScrollPane scrollPane;

    private static final String[][] RESPONSES = {
        {"anxious","anxiety","panic","nervous","worried","fear"},
        {"sad","cry","unhappy","depressed","hopeless","empty","lonely","alone"},
        {"stress","stressed","overwhelm","pressure","burden","too much"},
        {"happy","great","good","amazing","wonderful","excited","joy","fantastic"},
        {"tired","exhausted","sleep","fatigue","drained","no energy"},
        {"angry","frustrat","irritat","mad","upset","annoyed"},
        {"study","exam","test","grades","fail","college","assignment"},
        {"friend","relationship","family","breakup","fight","miss"},
        {"help","support","talk","listen","understand"},
        {"thank","thanks","appreciate","grateful"}
    };

    private static final String[][] REPLY_POOL = {
        {
            "I hear you. Anxiety can feel really overwhelming. Try this right now: breathe in for 4 seconds, hold for 4, breathe out for 4. You are safe.",
            "It is okay to feel anxious. Your body is just trying to protect you. Try grounding yourself — name 5 things you can see around you right now.",
            "Anxiety is tough but you are tougher. Try stepping outside for some fresh air. Even 5 minutes can help reset your mind."
        },
        {
            "I am really sorry you are feeling this way. Your feelings are completely valid. Would you like to talk about what is making you sad?",
            "Sadness is hard to carry. You do not have to carry it alone. Is there someone you can reach out to today, even just a quick message?",
            "It is okay to feel sad. Allow yourself to feel it without judgment. Sometimes watching something comforting or going for a walk can gently lift the mood."
        },
        {
            "Stress means you care — but do not let it consume you. Try writing down everything on your mind. Getting it out of your head really helps.",
            "When everything feels like too much, try breaking it into one small step. Just one. What is the smallest thing you can do right now?",
            "Take a 10 minute break — no phone, no work. Just breathe and rest. You will come back clearer and stronger."
        },
        {
            "That is wonderful to hear! What is making you feel so good? Hold onto that feeling!",
            "Love that energy! You deserve to feel this way. Keep doing whatever is working for you!",
            "Amazing! Happiness is contagious. Share it with someone today — it will multiply!"
        },
        {
            "Rest is so important and often underrated. Are you getting 7-8 hours of sleep? Try to wind down by 10pm and avoid screens before bed.",
            "When you are tired, even small tasks feel huge. Give yourself permission to rest. A 20 minute nap can genuinely recharge you.",
            "Drink some water, take a short walk, and if possible, rest. Your body and mind both need recovery time."
        },
        {
            "It is okay to feel frustrated. Take a deep breath and step away from the situation for a moment if you can.",
            "Anger is a valid emotion. Try to express it safely — write it out, go for a walk, or talk to someone you trust.",
            "When things feel irritating, sometimes the best thing is a short break. Give yourself space before responding or reacting."
        },
        {
            "Academic pressure is real and valid. Try breaking your study into 25 minute focused blocks with 5 minute breaks — it really works.",
            "Remember that your grades do not define your worth. Take it one step at a time and ask for help when you need it.",
            "Studying while stressed is hard. Try a quick 5 minute breathing exercise first, then get back to it with a clearer mind."
        },
        {
            "Relationships can be complicated and painful. It is okay to feel hurt. Give yourself time and space to process how you feel.",
            "Missing someone or dealing with conflict is really hard. Try to focus on what you can control and reach out if you need support.",
            "You deserve healthy, supportive relationships. If something feels off, trust your instincts and talk to someone you trust."
        },
        {
            "I am here for you. You can share anything with me. If you need more support, you can also message your therapist through the Therapist section.",
            "You reached out — that takes courage. Tell me more about what is going on and I will do my best to help.",
            "I am listening. Whatever you are going through, you do not have to face it alone."
        },
        {
            "You are very welcome! Remember, reaching out is a sign of strength. I am always here when you need to talk.",
            "Happy to help! Take care of yourself today.",
            "Anytime! You matter and your wellbeing matters. Come back whenever you need support."
        }
    };

    private static final String[] DEFAULT_REPLIES = {
        "Thank you for sharing that with me. Can you tell me a bit more about how you are feeling?",
        "I am here to listen. What is on your mind?",
        "That sounds important. Would you like to talk more about it?",
        "I want to make sure I understand. Can you share a little more?"
    };

    private final java.util.Random rand = new java.util.Random();

    public ChatbotPanel(User user) {
        setLayout(new BorderLayout(16, 16));
        setBackground(AppTheme.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 4));
        header.setBackground(AppTheme.BG_MAIN);
        JLabel title = new JLabel("AI Support Chatbot");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(AppTheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("A safe space to share how you feel. I am here to listen.");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 14));
        sub.setForeground(AppTheme.TEXT_SECONDARY);
        header.add(title);
        header.add(sub);
        add(header, BorderLayout.NORTH);

        chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(new Color(245, 248, 255));
        chatArea.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 240), 1, true));
        scrollPane.getViewport().setBackground(new Color(245, 248, 255));
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.setBackground(AppTheme.BG_MAIN);
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        JButton sendBtn = AppTheme.primaryButton("Send");
        sendBtn.setPreferredSize(new Dimension(100, 44));
        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(sendBtn, BorderLayout.EAST);
        add(inputRow, BorderLayout.SOUTH);

        addBotMessage("Hi " + user.getUsername() + "! I am here to support you. How are you feeling today? You can share anything with me.");

        ActionListener sendAction = e -> {
            String text = inputField.getText().trim();
            if (!text.isEmpty()) {
                addUserMessage(text);
                inputField.setText("");
                inputField.setEnabled(false);
                sendBtn.setEnabled(false);
                javax.swing.Timer t = new javax.swing.Timer(700, ev -> {
                    addBotMessage(getResponse(text));
                    inputField.setEnabled(true);
                    sendBtn.setEnabled(true);
                    inputField.requestFocus();
                });
                t.setRepeats(false);
                t.start();
            }
        };

        sendBtn.addActionListener(sendAction);
        inputField.addActionListener(sendAction);

        JPanel quickPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        quickPanel.setBackground(AppTheme.BG_MAIN);
        JLabel ql = new JLabel("Quick:");
        ql.setFont(new Font("SansSerif", Font.PLAIN, 12));
        ql.setForeground(AppTheme.TEXT_SECONDARY);
        quickPanel.add(ql);
        String[] quickReplies = {"I feel anxious", "I am stressed", "I feel sad", "I need help", "I feel happy"};
        for (String q : quickReplies) {
            JButton qb = new JButton(q);
            qb.setFont(new Font("SansSerif", Font.PLAIN, 12));
            qb.setBackground(new Color(225, 235, 255));
            qb.setForeground(AppTheme.PRIMARY);
            qb.setBorderPainted(false);
            qb.setFocusPainted(false);
            qb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            qb.addActionListener(e -> {
                addUserMessage(q);
                inputField.setEnabled(false);
                javax.swing.Timer t = new javax.swing.Timer(700, ev -> {
                    addBotMessage(getResponse(q));
                    inputField.setEnabled(true);
                    inputField.requestFocus();
                });
                t.setRepeats(false);
                t.start();
            });
            quickPanel.add(qb);
        }

        JPanel southPanel = new JPanel(new BorderLayout(0, 6));
        southPanel.setBackground(AppTheme.BG_MAIN);
        southPanel.add(quickPanel, BorderLayout.NORTH);
        southPanel.add(inputRow, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void addUserMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 2));
        row.setBackground(new Color(245, 248, 255));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        JLabel msg = new JLabel("<html><body style='width:280px;padding:2px'>" + text + "</body></html>");
        msg.setBackground(AppTheme.PRIMARY);
        msg.setForeground(Color.WHITE);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setOpaque(true);
        msg.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        row.add(msg);
        chatArea.add(row);
        chatArea.add(Box.createVerticalStrut(6));
        chatArea.revalidate();
        scrollToBottom();
    }

    private void addBotMessage(String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        row.setBackground(new Color(245, 248, 255));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        JLabel msg = new JLabel("<html><body style='width:280px;padding:2px'>" + text + "</body></html>");
        msg.setBackground(Color.WHITE);
        msg.setForeground(AppTheme.TEXT_PRIMARY);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msg.setOpaque(true);
        msg.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 220, 240), 1, true),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        row.add(msg);
        chatArea.add(row);
        chatArea.add(Box.createVerticalStrut(6));
        chatArea.revalidate();
        scrollToBottom();
    }

    private void scrollToBottom() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar v = scrollPane.getVerticalScrollBar();
            v.setValue(v.getMaximum());
        });
    }

    private String getResponse(String input) {
        String low = input.toLowerCase();

        boolean isNegative = low.contains("not") || low.contains("n't") ||
                             low.contains("never") || low.contains("no");

        if (isNegative) {
            if (low.contains("good") || low.contains("happy") || low.contains("fine") || low.contains("okay")) {
                return "I hear you. It sounds like you're not feeling your best today. Do you want to talk about what's been bothering you?";
            }
        }

        for (int i = 0; i < RESPONSES.length; i++) {
            for (String keyword : RESPONSES[i]) {
                if (low.contains(keyword)) {
                    String[] pool = REPLY_POOL[i];
                    return pool[rand.nextInt(pool.length)];
                }
            }
        }

        if (low.contains("not") || low.contains("bad") || low.contains("down")) {
            return "I'm really sorry you're feeling this way. I'm here for you — do you want to share more about what's going on?";
        }

        return DEFAULT_REPLIES[rand.nextInt(DEFAULT_REPLIES.length)];
    }
}