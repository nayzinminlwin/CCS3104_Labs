package Chap8;

public class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance -= amount;
    }

    public double getBalance() {
        return this.balance;
    }
}
// Test 1 : Standard Deposit: Verify that depositing 100 into an account with 50
// results in a balance of 150.

// Test 2 : Standard Withdrawal: Verify that withdrawing 30 from an account with
// 100 results in a balance of 70.

// Test 3 : Overdraft Protection: Verify that trying to withdraw more money than
// exists in the account throws an IllegalArgumentException.

// Test 4 : Negative Deposit: Verify that trying to deposit a negative amount
// throws an IllegalArgumentException.
