package inheritance;

public class SavingsAccount extends Account {
    double interestRate;

    //���캯��
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
