
import java.rmi.*;
import java.rmi.server.*;

public class CalcServer extends UnicastRemoteObject implements CalcInterface {

    private double radius;
    private double area;

    public CalcServer() throws RemoteException {
        super();
    }

    @Override
    public double findArea(double r) throws RemoteException {
        this.radius = r;
        this.area = Math.PI * Math.pow(radius, 2);
        System.out.println("Server : area is " + this.area);
        return this.area;
    }

}
