package Chap6.RMI_n_DB;

// ServerMain.java
import java.rmi.registry.LocateRegistry;
import java.rmi.Naming;

public class Server {
    public static void main(String[] args) throws Exception {
        // Optional: if clients connect from other hosts, set hostname to reachable IP:
        // System.setProperty("java.rmi.server.hostname", "YOUR_SERVER_IP");

        // Start an in-process registry on port 1099
        LocateRegistry.createRegistry(1099);

        DataServiceImpl impl = new DataServiceImpl();
        Naming.rebind("rmi://localhost:1099/DataService", impl);
        System.out.println("DataService bound. Server ready.");
    }
}
