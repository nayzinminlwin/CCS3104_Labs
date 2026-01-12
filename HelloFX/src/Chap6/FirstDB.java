package Chap6;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstDB {
        public static void main(String[] args)
                        throws SQLException, ClassNotFoundException {

                // Connect to a database
                Connection connection = DriverManager.getConnection(
                                "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm",
                                "A231198",
                                "231198");

                // test connection
                if (connection.isValid(0))
                        System.out.println("Database connected");

                // Create a statement
                Statement statement = connection.createStatement();

                // Execute a statement
                ResultSet resultSet = statement
                                .executeQuery("Select CustomerID, Name, Address from Avt_Customer");

                // Iterate through the result and print the student names
                while (resultSet.next())
                        System.out.println(resultSet.getString(1) + "\t" +
                                        resultSet.getString(2) + "\t" + resultSet.getString(3));

                // Close the connection
                connection.close();
        }

        public static Statement InitializeDB() {
                // dont throw exception here, handle it
                try {
                        // Connect to a database
                        Connection connection = DriverManager.getConnection(
                                        "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm",
                                        "A231198",
                                        "231198");

                        // test connection
                        if (connection.isValid(0))
                                System.out.println("Database connected");

                        // Create a statement
                        Statement statement = connection.createStatement();
                        return statement;
                } catch (SQLException ex) {
                        ex.printStackTrace();
                        System.out.println("Cannot connect to DB!!");
                        return null;
                }
        }
}