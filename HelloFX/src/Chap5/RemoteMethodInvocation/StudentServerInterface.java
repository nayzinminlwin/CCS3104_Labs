package Chap5.RemoteMethodInvocation;

import java.rmi.*;

// Step 1: Create the remote interface
public interface StudentServerInterface extends Remote {
    /**
     * Return the score for specified the name
     * 
     * @param name the student name
     * @return an double score or –1 if the student is not found
     */
    public double findScore(String name) throws RemoteException;
}
