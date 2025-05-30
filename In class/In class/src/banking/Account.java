package banking;

public class Account {
    private double balance;  //当前账户的余额

    //构造函数，接收一个参数用于初始化balance属性
    public Account(double _balance) {
        this.balance = _balance;

    }

    //用于获得当前的余额
    public double getBalance() {
        return balance;
    }

    //将参数中的余额添加到当前余额
    public void deposit(double deposit) {
        balance += deposit;

    }

    //从当前余额扣除参数中的余额
    public void withdraw(double withdraw) {
        balance -= withdraw;

    }
}
