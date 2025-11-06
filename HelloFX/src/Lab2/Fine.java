package Lab2;

public class Fine {

    public static double CarFine(int carSpeed, int speedLimit) {
        double fine = 0;
        if (carSpeed >= speedLimit) {
            fine = ((carSpeed - speedLimit) * (carSpeed - speedLimit) * 0.5);
        }
        System.out.println("Calculated car fine: " + fine);
        return fine;
    }

    public static double BikeFine(int bikeSpeed, int speedLimit) {
        double fine = 0;
        if (bikeSpeed >= speedLimit) {
            fine = (30 + (bikeSpeed - speedLimit));
        }
        System.out.println("Calculated bike fine: " + fine);
        return fine;
    }

}
