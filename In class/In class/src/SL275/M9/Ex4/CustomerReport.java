package SL275.M9.Ex4;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Iterator;

public class CustomerReport {

    public void generateReport() {
        NumberFormat currency_format = NumberFormat.getCurrencyInstance();
        currency_format.setCurrency(Currency.getInstance("USD"));

        Bank bank = Bank.getBank();/*** STEP 1: RETRIEVE BANK SINGLETON OBJECT HERE ***/
        //调用排序方法
        bank.sortCustomers();

        Customer customer;
        Iterator<Customer> customerIterator = bank.getCustomers();

        System.out.println("\t\t\tCUSTOMERS REPORT");
        System.out.println("\t\t\t================");

        while (customerIterator.hasNext()) {
            customer = customerIterator.next();

            System.out.println();
            System.out.println("Customer: "
                    + customer.getLastName() + ", "
                    + customer.getFirstName());

            Iterator<Account> accountIterator = customer.getAccounts(); // 使用getAccounts()方法获取迭代器

            while (accountIterator.hasNext()) {
                Account account = accountIterator.next();
                String account_type = "";

                // Determine the account type
                if (account instanceof SavingsAccount) {
                    account_type = "Savings Account";
                } else if (account instanceof CheckingAccount) {
                    account_type = "Checking Account";
                } else {
                    account_type = "Unknown Account Type";
                }


                // Print the current balance of the account
                System.out.println("    " + account_type + ": current balance is "
                        + currency_format.format(account.getBalance()));
            }
        }
    }

}
