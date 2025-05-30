import java.util.Iterator;

public class PlainTextSalesFormatter implements SalesFormatter {

    // 静态变量，保存单例实例
    private static PlainTextSalesFormatter singletonInstance;

    // 私有构造函数，防止其他类创建实例
    private PlainTextSalesFormatter() {

    }

    // 静态方法，获取单例实例
    public static PlainTextSalesFormatter getSingletonInstance() {
        if(singletonInstance==null){
            singletonInstance = new PlainTextSalesFormatter();
        }
        return singletonInstance;
    }

    // 实现 SalesFormatter 接口的 formatSales 方法
    @Override
    public String formatSales(Sales sales) {
        StringBuilder output = new StringBuilder();
        int ordernumber = 1;
        //输出每个订单
        Iterator<Order> orderIterator = sales.iterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            output.append("\n------------------------\n");
            output.append("\nOrder:").append(ordernumber++).append("\n");
            //输出每个订单项
            Iterator<OrderItem> itemIterator = order.iterator();
            while (itemIterator.hasNext()) {
                OrderItem item = itemIterator.next();
                Product product = item.getProduct();
                //Quantity
                output.append(item.getQuantity()).append(" ");
                //Code
                output.append(product.getCode()).append(" ");
                //Price
                output.append(product.getPrice()).append("\n");
            }
            //Total cost
            output.append("Total = ").append(order.getTotalCost()).append("\n");
        }
        return output.toString();
    }
}
