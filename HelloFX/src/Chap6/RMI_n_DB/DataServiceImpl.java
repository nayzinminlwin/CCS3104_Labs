package Chap6.RMI_n_DB;

// DataServiceImpl.java
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.sql.*;

public class DataServiceImpl extends UnicastRemoteObject implements DataService {

    private static final String DB_URL = "jdbc:oracle:thin:@fsktmdbora.upm.edu.my:1521:fsktm";
    private static final String DB_USER = "A231198";
    private static final String DB_PASSWORD = "231198";
    private Connection conn;

    protected DataServiceImpl() throws RemoteException {
        super();
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            prepTables();
        } catch (SQLException e) {
            throw new RemoteException("Unable to connect to database", e);
        }
    }

    private void prepTables() throws SQLException {

        // if table kv already exists, drop it first
        String dropTableSQL = "DROP TABLE kv";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            // Table might not exist; ignore
        }

        String createTableSQL = "CREATE TABLE kv (k VARCHAR2(50) PRIMARY KEY, value VARCHAR2(100))";
        String insertDataSQL = "INSERT INTO kv (k, value) VALUES (?, ?)";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            // Table might already exist; ignore
        }
        try (PreparedStatement ps = conn.prepareStatement(insertDataSQL)) {
            ps.setString(1, "welcome");
            ps.setString(2, "Hello, welcome to the RMI and DB example!");
            ps.executeUpdate();
        } catch (SQLException e) {
            // Data might already exist; ignore
        }
    }

    @Override
    public String getValueByKey(String key) throws RemoteException {
        // Minimal DB access (no pooling). Wrap SQLExceptions into RemoteException.
        String sql = "SELECT value FROM kv WHERE k = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, key);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("value"); // returned to client as a String
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("DB error in getValueByKey", e);
        }
    }
}
