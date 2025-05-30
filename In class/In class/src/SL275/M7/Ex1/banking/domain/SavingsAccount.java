package SL275.M7.Ex1.banking.domain;

public class SavingsAccount extends Account {
    private double interestRate;

    //¹¹Ôìº¯Êı
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
