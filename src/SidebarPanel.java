import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SidebarPanel extends JPanel {
    private String activeItem = "";
    private final java.util.List<SidebarItem> items = new java.util.ArrayList<>();

    public interface SelectionListener {
        void onSelect(String name);
    }

    private SelectionListener listener;

    public SidebarPanel(String username, boolean isAdmin) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.SIDEBAR_BG);
        setPreferredSize(new Dimension(200, 0));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(AppTheme.SIDEBAR_BG);
        top.setBorder(BorderFactory.createEmptyBorder(24, 16, 20, 16));

        JLabel logo = new JLabel("MindMate");
        logo.setFont(new Font("SansSerif", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);

        JLabel role = new JLabel(isAdmin ? "Therapist" : "Student");
        role.setFont(new Font("SansSerif", Font.PLAIN, 12));
        role.setForeground(AppTheme.SIDEBAR_TEXT);

        JLabel user = new JLabel(username);
        user.setFont(new Font("SansSerif", Font.BOLD, 13));
        user.setForeground(AppTheme.SIDEBAR_TEXT);

        JPanel userInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        userInfo.setBackground(AppTheme.SIDEBAR_BG);
        userInfo.add(user);
        userInfo.add(role);

        top.add(logo, BorderLayout.NORTH);
        top.add(userInfo, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(AppTheme.SIDEBAR_BG);
        nav.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        if (isAdmin) {
            addItem(nav, "Overview", "Overview");
            addItem(nav, "Mood Logs", "Mood Logs");
            addItem(nav, "Messages", "Messages");
            addItem(nav, "Analytics", "Analytics");
        } else {
            addItem(nav, "Dashboard", "Dashboard");
            addItem(nav, "Mood Tracker", "Mood Tracker");
            addItem(nav, "AI Chatbot", "AI Chatbot");
            addItem(nav, "Activities", "Activities");
            addItem(nav, "Habit Tracker", "Habit Tracker");
            addItem(nav, "Insights", "Insights");
            addItem(nav, "Therapist", "Therapist");
        }

        JScrollPane navScroll = new JScrollPane(nav);
        navScroll.setBorder(null);
        navScroll.setBackground(AppTheme.SIDEBAR_BG);
        navScroll.getViewport().setBackground(AppTheme.SIDEBAR_BG);
        add(navScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(AppTheme.SIDEBAR_BG);
        JButton logoutBtn = new JButton("Log Out");
        logoutBtn.setForeground(AppTheme.SIDEBAR_TEXT);
        logoutBtn.setBackground(AppTheme.SIDEBAR_BG);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
            new LoginFrame().setVisible(true);
        });
        bottom.add(logoutBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    private void addItem(JPanel nav, String label, String name) {
        SidebarItem item = new SidebarItem(label, name);
        items.add(item);
        nav.add(item);
        item.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                setActive(name);
                if (listener != null) listener.onSelect(name);
            }
            public void mouseEntered(MouseEvent e) {
                if (!item.isActive) item.setBackground(AppTheme.SIDEBAR_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                if (!item.isActive) item.setBackground(AppTheme.SIDEBAR_BG);
            }
        });
    }

    public void setActive(String name) {
        activeItem = name;
        for (SidebarItem item : items) {
            item.isActive = item.name.equals(name);
            item.setBackground(item.isActive ? AppTheme.SIDEBAR_ACTIVE : AppTheme.SIDEBAR_BG);
            item.label.setForeground(item.isActive ? Color.WHITE : AppTheme.SIDEBAR_TEXT);
        }
    }

    public void setSelectionListener(SelectionListener l) { this.listener = l; }

    static class SidebarItem extends JPanel {
        String name;
        JLabel label;
        boolean isActive = false;

        SidebarItem(String text, String name) {
            this.name = name;
            setLayout(new BorderLayout());
            setBackground(AppTheme.SIDEBAR_BG);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 16));
            label = new JLabel(text);
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));
            label.setForeground(AppTheme.SIDEBAR_TEXT);
            add(label, BorderLayout.CENTER);
        }
    }
}
