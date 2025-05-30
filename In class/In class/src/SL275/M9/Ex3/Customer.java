package SL275.M9.Ex3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private List<Account> accounts = new ArrayList<Account>();

    //构造函数
    public Customer(String f, String l) {
        this.firstname = f;
        this.lastname = l;
    }

    //访问firstname，返回属性值
    public String getFirstName() {
        return firstname;
    }

    //访问lastname，返回属性值
    public String getLastName() {
        return lastname;
    }

    //分配account属性
    public void setAccount(Account[] account) {
        this.accounts = new ArrayList<>();
    }

    //通过序号查找账户
    public Account getAccount(int index) {
        return accounts.get(index);
    }

    //返回账户的数量
    public int getNumOfAccounts() {
        return accounts.size();
    }

    //增加一个account对象
    public void addAccount(Account a) {
         this.accounts.add(a);
    }

    @Override
    public String toString() {
        return firstname + " " + lastname;
    }

    //返回迭代器
    public Iterator<Account> getAccounts() {
        return accounts.iterator();
    }
}
