package SL275.M4.Ex2;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private Account account;

    //构造函数
    public Customer(String f,String l){
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
    public void setAccount(Account acct) {
        this.account = acct;
    }

    //查询account属性
    public Account getAccount(){
        return account;
    }
}
