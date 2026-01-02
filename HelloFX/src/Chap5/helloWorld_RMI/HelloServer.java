package Chap5.helloWorld_RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

// extends UnicastRemoteObject to automatically export the object
public class HelloServer extends UnicastRemoteObject implements HelloService {

    public HelloServer() throws RemoteException {
        super();
    }

    // The actual implementation logic
    public String sayHello(String name) {
        return "Hello, " + name + "! (Message from Server)";
    }

    public static void main(String[] args) {
        try {
            // 1. Create the implementation object
            HelloService stub = new HelloServer();

            // 2. Create the registry on port 1099 (starts the phonebook)
            Registry registry = LocateRegistry.createRegistry(1099);

            // 3. Bind the object to a name
            registry.rebind("HelloService", stub);

            System.out.println("Server is ready and waiting...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}