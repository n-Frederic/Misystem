package SL275.M7.Ex1.banking.domain;


import SL275.M7.Ex1.banking.domain.Customer;

public class Bank {
    private Customer[] customers;
    private int numberOfCustomers;

    //私有构造函数
    private Bank() {
        this.customers = new Customer[6];
        this.numberOfCustomers = 0;
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
        customers[numberOfCustomers] = new Customer(f, l);
        numberOfCustomers++;
    }

    //获取用户的人数
    public int getNumOfCustomers() {
        return numberOfCustomers;
    }

    //根据序号的用户索引
    public Customer getCustomer(int a) {
        if (a >= 0 && a <= numberOfCustomers) {
            return customers[a];
        } else {
            return null;
        }
    }
}
