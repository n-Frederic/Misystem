package SL275.M5.Ex2;

public class TestBanking {
    public static void main(String[] args) {
        Bank bank = new Bank();
        bank.addCustomer("simms", "Jane");
        bank.addCustomer("Bryant", "owen");
        bank.addCustomer("Soley", "Tim");
        bank.addCustomer("Soley", "Marie");

        //打印用户信息
        for (int i = 0; i < bank.getNumOfCustomers(); i++) {
            System.out.println("customer[" + i + "] is " + bank.getCustomer(i));
        }
    }
}
