package Chap5.helloWorld_RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HelloService extends Remote {
    // All RMI methods must throw RemoteException
    String sayHello(String name) throws RemoteException;
}