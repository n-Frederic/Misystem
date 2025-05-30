import java.util.*;

public class Sales implements Iterable<Order> {

    List<Order> orders;

    //返回一个迭代器，用于遍历集合
    @Override
    public Iterator<Order> iterator() {
        return orders.iterator();
    }
    //创建订单集合，初始化为空
    public Sales() {
        this.orders = new ArrayList<>();
    }

    //增加订单
    public void addOrder(Order order){
        orders.add(order);
    }

    //获取订单总数
    public int getNumberOfOrders(){
        return orders.size();
    }
}
