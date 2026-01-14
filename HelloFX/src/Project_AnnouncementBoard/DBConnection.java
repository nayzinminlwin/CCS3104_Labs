package Project_AnnouncementBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    public static void main(String args[]) {
        DeleteAnnouncement(7); // Example usage
    }

    // Method to delete an announcement by its ID
    public static void DeleteAnnouncement(int announcementId) {
        String deleteSQL = "DELETE FROM ab_posts WHERE post_id = ?";
        try (Connection connection = getConnection();
                var preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, announcementId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Announcement deleted successfully.");
            } else {
                System.out.println("No announcement found with the given ID.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Error deleting announcement: " + ex.getMessage());
        }
    }

    // Database connection constants
    private static final String DB_URL = "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm";
    private static final String DB_USER = "A231198";
    private static final String DB_PASSWORD = "231198";

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        if (connection.isValid(0)) {
            System.out.println("Database connected");
        }
        return connection;
    }

    @Deprecated
    public static Statement getStatement() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (connection.isValid(0)) {
                System.out.println("Database connected");
            }
            return connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.err.println("Cannot connect to database: " + ex.getMessage());
            return null;
        }
    }
}
