package inheritance3;

public class SavingsAccount extends Account {
    double interestRate;


    //¹¹Ôìº¯Êı
    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }
}
