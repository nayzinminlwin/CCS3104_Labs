package Chap6.RMI_n_DB;

// ClientMain.java
import java.rmi.Naming;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        // Use server hostname or IP here (replace localhost if remote)
        String serverHost = "localhost";
        DataService svc = (DataService) Naming.lookup("rmi://" + serverHost + ":1099/DataService");

        System.out.println(svc);

        if (svc == null) {
            System.out.println("DataService lookup failed!");
            return;
        }

        String key = "welcome";
        String value = svc.getValueByKey(key);
        System.out.println("value for key='" + key + "' -> " + value);
    }
}
