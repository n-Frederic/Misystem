package SL275.M7.Ex1.banking.domain;

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
    public void withdraw(double amt) throws OverdraftException {
        //余额充足，取款成功
        if (amt <= balance) {
            balance -= amt;
        } else {
            //如果有余额保护
            if (overdraftProtection > 0) {
                //判断是否超过余额保护
                if (overdraftProtection + balance >= amt) {
                    overdraftProtection -= (amt - balance);
                    balance = 0;
                } else {
                    double deficit;
                    deficit = amt - balance;
                    throw new OverdraftException("Insufficient funds for overdraft protection", deficit);
                }
            //否则抛出无余额保护的异常
            } else {
                throw new OverdraftException("No overdraft protection", amt - balance);
            }
        }
    }
}
