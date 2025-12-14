package Chap4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

public class AccountWithoutSync {

    private static Account ac = new Account();
    private static Random rand = new Random();

    private static class AddAPennyTask implements Runnable {

        @Override
        public void run() {
            ac.deposit(1);
        }
    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.execute(new AddAPennyTask());
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        ac.printBalance();
    }

    private static class Account {
        private int balance;

        Account() {
            this.balance = 0;
        }

        public int getBalance() {
            return this.balance;
        }

        public void printBalance() {
            System.out.println("What is balance ? " + getBalance());
        }

        public void deposit(int amount) {
            int newBalance = getBalance() + amount;
            printBalance();

            try {
                // get a random sleep time between 1 to 5 milliseconds
                Thread.sleep(rand.nextInt(5) + 1);
            } catch (InterruptedException ex) {

            }

            this.balance = newBalance;
        }

    }
}