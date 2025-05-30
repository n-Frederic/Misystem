package inheritance;

import inheritance.Account;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private inheritance.Account account;

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
    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    //查询account属性
    public Account account() {
        return account;
    }

    public String toString() {
        return firstname + " " + lastname;
    }

}
