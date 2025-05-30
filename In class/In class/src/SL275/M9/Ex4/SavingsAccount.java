package SL275.M9.Ex4;

public class SavingsAccount extends Account {
    private double interestRate;

    //���캯��
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
