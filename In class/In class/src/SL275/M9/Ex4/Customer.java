package SL275.M9.Ex4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Customer implements Comparable<Customer> {
    //声明三个私有属性
    private String firstname;
    private String lastname;
    private List<Account> accounts = new ArrayList<Account>();

    //实现Comparable中的compareTo方法

//如果返回值为负数，表示 this.lastName 在字典顺序上小于 o.lastName，因此 this 顾客应该排在 o 顾客之前。
//如果返回值为正数，表示 this.lastName 在字典顺序上大于 o.lastName，因此 this 顾客应该排在 o 顾客之后。
//如果返回值为 0，表示两个顾客的姓氏相同，我们需要继续比较名字。
    @Override
    public int compareTo(Customer o) {
        //先比较姓氏
        if(this.lastname.compareTo(o.getLastName()) != 0){
            return this.lastname.compareTo(o.getLastName());
        }else{  //如果形式相同，比较名字
            return this.firstname.compareTo(o.getFirstName());
        }
    }

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
