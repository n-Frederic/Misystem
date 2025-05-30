package inheritance2;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private Account[] accounts = new Account[10];
    private int numOfAccounts;

    //构造函数
    public Customer(String f, String l) {
        this.firstname = f;
        this.lastname = l;
        this.numOfAccounts = 0;
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
        this.accounts = account;
    }

    //通过序号查找账户
    public Account getAccount(int index) {
        return accounts[index];
    }

    //返回账户的数量
    public int getNumOfAccounts() {
        return numOfAccounts;
    }

    //增加一个account对象
    public Account addAccount(Account a) {
        if (numOfAccounts <= 10) {
            this.accounts[numOfAccounts] = a;
            numOfAccounts++;
            return a;
        } else {
            return null;
        }
    }

    public String toString() {
        return firstname + " " + lastname;
    }

}
