package Chap5.helloWorld_RMI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class HelloClient {
    public static void main(String[] args) {
        try {
            // 1. Find the Registry (Phonebook)
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // 2. Lookup the object by name (Must cast it!)
            HelloService stub = (HelloService) registry.lookup("HelloService");

            // 3. Invoke the method
            // The string "Alex" is passed by Value (Serialized)
            String response = stub.sayHello("Alex");

            System.out.println("Response: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}