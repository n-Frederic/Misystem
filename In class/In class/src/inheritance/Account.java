package inheritance;

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
    public boolean withdraw(double withdraw) {
        if (balance >= withdraw) {
            balance -= withdraw;
            return true;
        } else {
            return false;
        }
    }
}
