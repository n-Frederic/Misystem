package SL275.M7.Ex1.banking.domain;

public class Account {
    protected double balance;  //当前账户的余额

    //无参的构造函数
    public Account() {
    }

    //构造函数，接收一个参数用于初始化balance属性
    public Account(double _balance) {
        this.balance = _balance;

    }

    //用于获得当前的余额
    public double getBalance() {
        return balance;
    }

    //将参数中的余额添加到当前余额
    public boolean deposit(double deposit) {
        balance += deposit;
        return true;
    }

    //从当前余额扣除参数中的余额
    public void withdraw(double withdraw) throws OverdraftException {
        if (balance >= withdraw) {
            balance -= withdraw;
        } else {
            double deficit;
            deficit=withdraw-balance;
            throw new OverdraftException("Insufficient funds",deficit);
        }
    }
}
