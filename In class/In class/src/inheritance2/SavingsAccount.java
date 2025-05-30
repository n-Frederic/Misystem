package inheritance2;

public class SavingsAccount extends Account {
    private double interestRate;

    //¹¹Ôìº¯Êı
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
