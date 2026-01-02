
import java.rmi.*;

public interface CalcInterface extends Remote {
    public double findArea(double r) throws RemoteException;
}
