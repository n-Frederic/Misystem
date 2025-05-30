package SL275.M3.Ex2;

public class TestBanking {

    public static void main(String[] args) {
        Account  account;
        System.out.println("Creating a customer Jane Smith");
        Customer c = new Customer("Jane","Smith");

        // Create an account that can has a 500.00 balance.
        System.out.println("Creating her account with a 500.00 balance.");
        account = new Account(500.00);

        System.out.println("Withdraw 150.00");
        account.withdraw(150.00);

        System.out.println("Deposit 22.50");
        account.deposit(22.50);

        System.out.println("Withdraw 47.62");
        account.withdraw(47.62);

        // Print out the final account balance
        System.out.println("Customer["+c.getFirstname()+" "+c.getLastname()+"] has a balance of " + account.getBalance());

    }
}

