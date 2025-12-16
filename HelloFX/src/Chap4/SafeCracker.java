package Chap4;

import java.util.Random;

public class SafeCracker {
    // THE KILL SWITCH
    // 'volatile' guarantees that if one thread changes this,
    // all other threads see the change immediately.
    public static volatile boolean isCodeCracked = false;

    public static void main(String[] args) {
        // The secret number we are looking for
        int secretCode = 999;

        // Create 3 Hacker threads
        Thread hacker1 = new Thread(new HackerTask("Hacker-A", secretCode));
        Thread hacker2 = new Thread(new HackerTask("Hacker-B", secretCode));
        Thread hacker3 = new Thread(new HackerTask("Hacker-C", secretCode));

        hacker1.start();
        hacker2.start();
        hacker3.start();
    }
}

class HackerTask implements Runnable {
    private String hackerName;
    private int targetCode;
    private Random random = new Random();

    public HackerTask(String name, int target) {
        this.hackerName = name;
        this.targetCode = target;
    }

    @Override
    public void run() {
        // THE CHECK
        // Every iteration, we check the global kill switch.
        // If isCodeCracked is true, this loop breaks immediately.
        while (!SafeCracker.isCodeCracked) {

            // 1. Do some work (Guess a number)
            int guess = random.nextInt(1000);

            System.out.println(hackerName + " guessed: " + guess);

            // 2. Check if we succeeded
            if (guess == targetCode) {
                // 3. FLIP THE SWITCH
                SafeCracker.isCodeCracked = true;

                System.out.println("-----------------------------------");
                System.out.println(hackerName + " FOUND THE CODE: " + guess);
                System.out.println("TELLING EVERYONE TO STOP!");
                System.out.println("-----------------------------------");
                return; // End this thread
            }
        }

        // If we get here, it means another thread flipped the switch
        System.out.println(hackerName + ": " + "Someone else found it. I am stopping.");
    }
}