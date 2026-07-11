import java.sql.*;

public class DatabaseHelper {
    private static final String DB_PATH = "mindmate.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "username TEXT UNIQUE NOT NULL,"
            + "password TEXT NOT NULL,"
            + "role TEXT NOT NULL DEFAULT 'user'"
            + ");";

        String createMoodLogs = "CREATE TABLE IF NOT EXISTS mood_logs ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "mood TEXT NOT NULL,"
            + "note TEXT,"
            + "log_date TEXT NOT NULL,"
            + "FOREIGN KEY (user_id) REFERENCES users(id)"
            + ");";

        String createMessages = "CREATE TABLE IF NOT EXISTS messages ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "username TEXT NOT NULL,"
            + "message TEXT NOT NULL,"
            + "sent_date TEXT NOT NULL,"
            + "is_read INTEGER DEFAULT 0,"
            + "FOREIGN KEY (user_id) REFERENCES users(id)"
            + ");";

        String insertAdmin = "INSERT OR IGNORE INTO users (username, password, role) "
            + "VALUES ('admin', 'admin123', 'admin');";

        String insertUser = "INSERT OR IGNORE INTO users (username, password, role) "
            + "VALUES ('student1', 'pass123', 'user');";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createMoodLogs);
            stmt.execute(createMessages);
            stmt.execute(insertAdmin);
            stmt.execute(insertUser);
            System.out.println("Database ready.");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }
}
private static final String URL = "jdbc:sqlite:" + DB_PATH;

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initialize() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "username TEXT UNIQUE NOT NULL,"
            + "password TEXT NOT NULL,"
            + "role TEXT NOT NULL DEFAULT 'user'"
            + ");";

        String createMoodLogs = "CREATE TABLE IF NOT EXISTS mood_logs ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "mood TEXT NOT NULL,"
            + "note TEXT,"
            + "log_date TEXT NOT NULL,"
            + "FOREIGN KEY (user_id) REFERENCES users(id)"
            + ");";

        String createMessages = "CREATE TABLE IF NOT EXISTS messages ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "user_id INTEGER NOT NULL,"
            + "username TEXT NOT NULL,"
            + "message TEXT NOT NULL,"
            + "sent_date TEXT NOT NULL,"
            + "is_read INTEGER DEFAULT 0,"
            + "FOREIGN KEY (user_id) REFERENCES users(id)"
            + ");";

        String insertAdmin = "INSERT OR IGNORE INTO users (username, password, role) "
            + "VALUES ('admin', 'admin123', 'admin');";

        String insertUser = "INSERT OR IGNORE INTO users (username, password, role) "
            + "VALUES ('student1', 'pass123', 'user');";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createMoodLogs);
            stmt.execute(createMessages);
            stmt.execute(insertAdmin);
            stmt.execute(insertUser);
            System.out.println("Database ready.");
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
        }
    }
}