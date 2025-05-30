package inheritance2;

public class CheckingAccount extends Account {
    double overdraftProtection;

    //构造器
    public CheckingAccount(double balance) {
        super(balance);
        this.balance = balance;
    }

    //另一个构造器
    public CheckingAccount(double balance, double protect) {
        super(balance);
        this.balance = balance;
        this.overdraftProtection = protect;
    }

    //取钱
    public boolean withdraw(double amt) {
        //余额充足，取款成功
        if (amt <= balance) {
            balance -= amt;
            return true;
        } else {
            //判断是否超过余额保护
            if (overdraftProtection + balance >= amt) {
                overdraftProtection -= (amt - balance);
                balance = 0;
                return true;
            } else {
                return false;
            }
        }
    }
}
