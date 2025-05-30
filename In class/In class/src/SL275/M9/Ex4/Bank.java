package SL275.M9.Ex4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Bank {

    private List<Customer> customers;

    //私有构造函数
    private Bank() {
        this.customers = new ArrayList<>();
    }

    //私有静态实例
    public static Bank instance;

    // 公共静态方法，返回Bank的实例
    public static Bank getBank() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    //增加一个用户
    public void addCustomer(String f, String l) {
        this.customers.add(new Customer(f, l));
    }

    //获取用户的人数
    public int getNumOfCustomers() {
        return customers.size();
    }

    //根据序号的用户索引
    public Customer getCustomer(int a) {
        return customers.get(a);
    }

    //返回一个迭代器
    public Iterator<Customer> getCustomers() {
        return customers.iterator();
    }

    //按名字对用户进行排序
    public void sortCustomers(){
        Collections.sort(customers);
    }
}
