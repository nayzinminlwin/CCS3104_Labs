package Chap6.RMI_n_DB;

// DataService.java
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DataService extends Remote {
    // client asks server for a String identified by key
    String getValueByKey(String key) throws RemoteException;
}
