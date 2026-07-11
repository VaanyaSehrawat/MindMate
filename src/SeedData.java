import java.sql.*;
import java.time.LocalDate;

public class SeedData {
    public static void main(String[] args) {
        DatabaseHelper.initialize();
        try (Connection conn = DatabaseHelper.connect();
             Statement stmt = conn.createStatement()) {

            System.out.println("Inserting users...");
            String[] users = {
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('priya', 'pass123', 'user');",
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('arjun', 'pass123', 'user');",
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('neha', 'pass123', 'user');",
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('riya', 'pass123', 'user');",
                "INSERT OR IGNORE INTO users (username, password, role) VALUES ('kabir', 'pass123', 'user');"
            };
            for (String s : users) stmt.execute(s);

            System.out.println("Inserting mood logs...");
            String[][] logs = {
                {"priya",  "2026-04-01", "Anxious",  "Exam tomorrow, very nervous"},
                {"priya",  "2026-04-02", "Stressed",  "Couldn't sleep at all"},
                {"priya",  "2026-04-03", "Calm",      "Meditation helped a lot today"},
                {"priya",  "2026-04-04", "Happy",     "Exam went well!"},
                {"priya",  "2026-04-05", "Happy",     "Had a great day with friends"},
                {"arjun",  "2026-04-01", "Tired",     "Too many assignments this week"},
                {"arjun",  "2026-04-02", "Stressed",  "Presentation stress"},
                {"arjun",  "2026-04-03", "Anxious",   "Worried about results"},
                {"arjun",  "2026-04-04", "Calm",      "Took a long walk, felt better"},
                {"arjun",  "2026-04-05", "Happy",     "Got good grades!"},
                {"neha",   "2026-04-01", "Sad",       "Missing home a lot"},
                {"neha",   "2026-04-02", "Sad",       "Feeling very lonely today"},
                {"neha",   "2026-04-03", "Anxious",   "Social anxiety acting up"},
                {"neha",   "2026-04-04", "Calm",      "Called mom, felt much better"},
                {"neha",   "2026-04-05", "Happy",     "Made a new friend today"},
                {"riya",   "2026-04-01", "Happy",     "Started journaling again"},
                {"riya",   "2026-04-02", "Calm",      "Yoga session was amazing"},
                {"riya",   "2026-04-03", "Tired",     "Late night study session"},
                {"riya",   "2026-04-04", "Stressed",  "Group project deadlines"},
                {"riya",   "2026-04-05", "Calm",      "Submitted project, relieved"},
                {"kabir",  "2026-04-01", "Stressed",  "Financial stress"},
                {"kabir",  "2026-04-02", "Anxious",   "Job interview coming up"},
                {"kabir",  "2026-04-03", "Tired",     "Barely slept"},
                {"kabir",  "2026-04-04", "Happy",     "Interview went great!"},
                {"kabir",  "2026-04-05", "Calm",      "Taking it one day at a time"}
            };

            for (String[] log : logs) {
                ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE username='" + log[0] + "'");
                if (rs.next()) {
                    int uid = rs.getInt("id");
                    stmt.execute("INSERT OR IGNORE INTO mood_logs (user_id, mood, note, log_date) VALUES ("
                        + uid + ", '" + log[2] + "', '" + log[3] + "', '" + log[1] + "');");
                }
            }

            System.out.println("Inserting messages...");
            String[][] messages = {
                {"priya",  "2026-04-02", "I have been feeling really anxious about my exams. I cannot sleep properly.", "It is completely normal to feel this way before exams. Try box breathing before bed — inhale 4s, hold 4s, exhale 4s. You are more prepared than you think!"},
                {"neha",   "2026-04-02", "I feel very lonely here. I miss my family and do not know how to make friends.", "Loneliness is one of the hardest feelings. You are not alone in feeling this way. Try joining one campus club or activity — even one connection can change everything."},
                {"arjun",  "2026-04-03", "The pressure of studies and expectations is getting too much for me.", "Academic pressure is real and valid. Remember to take breaks — you cannot pour from an empty cup. Let us talk about what specifically feels most overwhelming."},
                {"kabir",  "2026-04-02", "I am very nervous about my job interview tomorrow. What if I fail?", "Nervousness before interviews is completely natural — it means you care. Prepare your key points, get good sleep tonight, and remember: one interview does not define your worth."},
                {"riya",   "2026-04-03", "I feel stressed about group projects. My team is not cooperating.", null}
            };

            for (String[] msg : messages) {
                ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE username='" + msg[0] + "'");
                if (rs.next()) {
                    int uid = rs.getInt("id");
                    String reply = msg[4] != null ? "'" + msg[4] + "'" : "NULL";
                    stmt.execute("INSERT INTO messages (user_id, username, message, sent_date, reply) VALUES ("
                        + uid + ", '" + msg[0] + "', '" + msg[2] + "', '" + msg[1] + "', " + reply + ");");
                }
            }

            System.out.println("Done! Dummy data inserted successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
