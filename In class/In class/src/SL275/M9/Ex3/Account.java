package SL275.M9.Ex3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Account {
    protected double balance;  // 账户余额

    // 无参构造函数，初始化时不设置余额
    public Account() {
    }

    // 带参构造函数，用于初始化账户余额
    public Account(double _balance) {
        this.balance = _balance;
    }

    // 获取账户余额的方法
    public double getBalance() {
        return balance;
    }

    // 存款方法，向账户中存入一定金额
    public boolean deposit(double deposit) {
        balance += deposit;
        return true;
    }

    // 取款方法，从账户中提取一定金额，若余额不足则抛出异常
    public void withdraw(double withdraw) throws OverdraftException {
        if (balance >= withdraw) {
            balance -= withdraw;
        } else {
            double deficit;
            deficit = withdraw - balance;
            throw new OverdraftException("Insufficient funds", deficit);
        }
    }


}
