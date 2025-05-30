import java.util.*;

public class Order implements Iterable<OrderItem> {

    List<OrderItem> items;

    //返回一个迭代器，用于遍历集合
    @Override
    public Iterator<OrderItem> iterator() {
        return items.iterator();
    }

    //创建订单项集合，初始化为空
    public Order() {
        this.items = new ArrayList<>();
    }

    //向集合中增加一个订单项
    public void addItem(OrderItem orderItem) {
        this.items.add(orderItem);
    }

    //在集合中删除一个订单项
    public void removeItem(OrderItem orderItem) {
        this.items.remove(orderItem);
    }

    //通过产品信息查找订单项
    public OrderItem getItem(Product product) {
        for (OrderItem item : items) {
            if (item.getProduct().equals(product)) {
                return item;
            }
        }
        return null;
    }

    //获取订单项总数
    public int getNumberOfItems() {
        return items.size();
    }

    //获取订单项的总价格
    public double getTotalCost() {
        double TotalCost = 0;
        for (OrderItem item : items) {
            TotalCost += item.getProduct().getPrice() * item.getQuantity();
        }
        return TotalCost;
    }
}
