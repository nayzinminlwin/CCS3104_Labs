package Lab2;

public class Fine {

    public static double VehicleFine(String vehicleType, int speed) {
        double fine = 0;
        int speedLimit = 0;
        vehicleType = vehicleType.trim().toLowerCase();

        if (vehicleType.equals("car")) {
            speedLimit = 110;
            if (speed >= speedLimit) {
                fine = ((speed - speedLimit) * (speed - speedLimit) * 0.5);
            }
        } else if (vehicleType.equals("bike")) {
            speedLimit = 70;
            if (speed >= speedLimit) {
                fine = (30 + (speed - speedLimit));
            }
        }
        System.out.println("Calculated fine: " + fine);
        return fine;
    }

}
