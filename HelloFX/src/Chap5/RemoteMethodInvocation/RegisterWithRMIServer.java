package Chap5.RemoteMethodInvocation;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

// step 3: Create the RMI server
public class RegisterWithRMIServer {
    /** Main method */
    public static void main(String[] args) {
        try {
            StudentServerInterface obj = new StudentServerInterfaceImpl();
            Registry registry;

            try {
                registry = LocateRegistry.createRegistry(1099);
                System.out.println("Created new RMI registry on port 1099");
            } catch (RemoteException createEx) {
                registry = LocateRegistry.getRegistry(1099);
                System.out.println("Using existing RMI registry on port 1099");
            }

            registry.rebind("StudentServerInterfaceImpl", obj);
            System.out.println("Student server " + obj + " registered");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}