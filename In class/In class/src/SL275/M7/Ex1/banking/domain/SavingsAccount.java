package SL275.M7.Ex1.banking.domain;

public class SavingsAccount extends Account {
    private double interestRate;

    //���캯��
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
