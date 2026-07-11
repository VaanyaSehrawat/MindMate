public class Main {
    public static void main(String[] args) {
        DatabaseHelper.initialize();
        javax.swing.SwingUtilities.invokeLater(() -> {
            new HomePanel().setVisible(true);
        });
    }
}
