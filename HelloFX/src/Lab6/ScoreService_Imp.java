package Lab6;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;

public class ScoreService_Imp extends UnicastRemoteObject implements ScoreService_Interface {

    private static final String DB_URL = "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm";
    private static final String DB_USER = "A231198";
    private static final String DB_PASSWORD = "231198";
    private Connection conn;

    ScoreService_Imp() throws RemoteException {
        super();
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            prepTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prepTables() {
        // if table already exists, drop it first
        String dropTableSQL = "DROP TABLE scores";
        try (var stmt = conn.createStatement()) {
            stmt.executeUpdate(dropTableSQL);
            System.out.println("Table dropped successfully");
        } catch (Exception e) {
            System.out.println("Table doesn't exist yet, will create new one");
        }

        String createTableSQL = "CREATE TABLE scores (name VARCHAR2(50), score NUMBER, permission NUMBER(1))";
        try (var stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Table created successfully");
        } catch (Exception e) {
            System.out.println("Error creating table:");
            e.printStackTrace();
        }

        prepInitialData();
    }

    public void prepInitialData() {
        // insert into Scores values ('John', 90.5, true);
        // insert into Scores values ('Michael', 100, true);
        // insert into Scores values ('Michelle', 100, false);

        String insertDataSQL = "INSERT INTO scores (name, score, permission) VALUES (?, ?, ?)";
        try (var ps = conn.prepareStatement(insertDataSQL)) {
            ps.setString(1, "John");
            ps.setInt(2, 90);
            ps.setInt(3, 1); // 1 = true
            ps.executeUpdate();

            ps.setString(1, "Michael");
            ps.setInt(2, 100);
            ps.setInt(3, 1); // 1 = true
            ps.executeUpdate();

            ps.setString(1, "Michelle");
            ps.setInt(2, 100);
            ps.setInt(3, 0); // 0 = false
            ps.executeUpdate();

            // conn.commit();
            System.out.println("Initial data inserted successfully");

        } catch (Exception e) {
            System.out.println("Error inserting data:");
            e.printStackTrace();
        }
    }

    @Override
    public String getScores(String name) throws RemoteException {

        String selectSQL = "SELECT score, permission FROM scores WHERE name = ?";
        try (var ps = conn.prepareStatement(selectSQL)) {
            ps.setString(1, name);
            try (var rs = ps.executeQuery()) {
                StringBuilder scores = new StringBuilder();
                // check if result is null, then return "Not Found!"
                if (!rs.isBeforeFirst()) {
                    return "Not Found!";
                }

                while (rs.next()) {
                    int permission = rs.getInt("permission");
                    if (permission == 0) { // 0 = false
                        return "Not Permitted!";
                    }
                    scores.append(rs.getInt("score")).append(", ");
                }
                if (scores.length() > 0) {
                    scores.setLength(scores.length() - 2); // Remove trailing comma and space
                }
                return scores.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "No Found!";
        }
    }

}
