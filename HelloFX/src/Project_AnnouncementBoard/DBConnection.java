package Project_AnnouncementBoard;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
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
