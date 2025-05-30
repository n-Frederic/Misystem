package inheritance3;


public class Bank {
    private Customer[] customers;
    private int numberOfCustomers;

    //构造函数
    public Bank() {
        this.customers = new Customer[6];
        this.numberOfCustomers = 0;
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
