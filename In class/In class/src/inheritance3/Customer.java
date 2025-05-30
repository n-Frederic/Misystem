package inheritance3;

public class Customer {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private inheritance3.Account account;
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;

    public SavingsAccount getSavings() {
        return savingsAccount;
    }

    public void setSavings(SavingsAccount savingsAccount) {
        this.savingsAccount = savingsAccount;
    }

    public CheckingAccount getChecking() {
        return checkingAccount;
    }

    public void setChecking(CheckingAccount checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    //构造函数
    public Customer(String f, String l) {
        this.firstname = f;
        this.lastname = l;
        this.savingsAccount = null;
        this.checkingAccount = null;
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

    //重写toString
    public String toString() {
        return firstname + " " + lastname;
    }
}
