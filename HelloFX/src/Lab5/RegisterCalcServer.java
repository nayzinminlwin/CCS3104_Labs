
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RegisterCalcServer {
    public static void main(String[] args) {
        try {
            // Create the CalcServer object
            CalcServer calcServer = new CalcServer();
            Registry registry;

            try {
                // Try to create a new registry
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Created new RMI registry on port 1099");
            } catch (RemoteException createEx) {
                // If registry already exists, get it
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Using existing RMI registry on port 1099");
            }

            // Bind the CalcServer to the registry with name "CalcServer"
            registry.rebind("CalcServer", calcServer);
            System.out.println("CalcServer " + calcServer + " registered successfully");
            System.out.println("Server is ready and waiting for client connections...");

            // Keep the server running
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            System.err.println("Server error:");
            ex.printStackTrace();
        }
    }
}
