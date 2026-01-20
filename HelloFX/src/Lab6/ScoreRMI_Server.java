package Lab6;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ScoreRMI_Server {
    public static void main(String[] args) throws Exception {

        try {
            System.out.println("Score RMI Server");

            Registry reg = LocateRegistry.createRegistry(1099);

            ScoreService_Interface scoreService = new ScoreService_Imp();
            reg.rebind("ScoreService", scoreService);
            System.out.println("ScoreService bound. Server ready.");

            // Keep the server running
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            // e.printStackTrace();
        }

    }

}
