package SL275.M5.Ex2;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private Account account;

    //构造函数
    public Customer(String f, String l) {
        this.firstname = f;
        this.lastname = l;
    }

    //访问firstname，返回属性值
    public String getFirstname() {
        return firstname;
    }

    //访问lastname，返回属性值
    public String getLastname() {
        return lastname;
    }

    //分配account属性
    public void setAccount(Account account) {
        this.account = account;
    }

    //查询account属性
    public Account account() {
        return account;
    }

    public String toString() {
        return firstname + " " + lastname;
    }
}
