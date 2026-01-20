package Lab6;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ScoreService_Interface extends Remote {

    String getScores(String name) throws RemoteException;
}
