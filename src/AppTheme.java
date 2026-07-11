import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class AppTheme {

    // ================= COLORS =================
    public static final Color PRIMARY = new Color(52, 152, 219);
    public static final Color PRIMARY_DARK = new Color(41, 128, 185);
    public static final Color PRIMARY_LIGHT = new Color(93, 173, 226);

    public static final Color ACCENT_GREEN = new Color(46, 204, 113);
    public static final Color ACCENT_RED = new Color(231, 76, 60);
    public static final Color ACCENT_ORANGE = new Color(243, 156, 18);
    public static final Color ACCENT_TEAL = new Color(26, 188, 156);

    public static final Color SUCCESS = ACCENT_GREEN;
    public static final Color DANGER = ACCENT_RED;

    public static final Color TEXT_PRIMARY = new Color(40, 40, 40);
    public static final Color TEXT_SECONDARY = new Color(120, 120, 120);
    public static final Color TEXT_MUTED = new Color(150, 150, 150);

    public static final Color BORDER = new Color(210, 220, 240);

    // BACKGROUND COLORS
    public static final Color BG_MAIN = new Color(245, 247, 250);
    public static final Color BG_CARD = Color.WHITE;

    // SIDEBAR COLORS
    public static final Color SIDEBAR_BG = new Color(30, 30, 30);
    public static final Color SIDEBAR_TEXT = new Color(220, 220, 220);
    public static final Color SIDEBAR_HOVER = new Color(50, 50, 50);
    public static final Color SIDEBAR_ACTIVE = new Color(70, 130, 180);

    // ================= FONTS =================
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 18);
    public static final Font FONT_BODY = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 12);

    // ================= CARD =================
    public static JPanel card(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(BG_CARD);
        p.setBorder(cardBorder());

        if (title != null) {
            JLabel lbl = new JLabel(title);
            lbl.setFont(FONT_HEADING);
            lbl.setForeground(TEXT_PRIMARY);
            p.add(lbl, BorderLayout.NORTH);
        }

        return p;
    }

    // 🔥 MISSING METHOD FIXED
    public static Border cardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );
    }

    // ================= BUTTONS =================
    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getModel().isRollover() ? PRIMARY_DARK : PRIMARY);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        styleButton(btn);
        return btn;
    }

    public static JButton accentButton(String text, Color color) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(getModel().isRollover() ? color.darker() : color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        styleButton(btn);
        return btn;
    }

    public static JButton makeButton(String text, Color color) {
        return accentButton(text, color);
    }

    private static void styleButton(JButton btn) {
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(FONT_BODY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ================= INPUT =================
    public static Border inputBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    // ================= ROUNDED BORDER =================
    public static class RoundedBorder implements Border {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}