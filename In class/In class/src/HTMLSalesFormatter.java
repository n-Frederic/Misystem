import java.util.Iterator;

public class HTMLSalesFormatter implements SalesFormatter {

    // 静态变量，保存单例实例
    private static HTMLSalesFormatter singletonInstance = new HTMLSalesFormatter();

    // 私有构造函数，防止其他类创建实例
    private HTMLSalesFormatter() {

    }

    // 静态方法，获取单例实例
    public static HTMLSalesFormatter getSingletonInstance() {
        return singletonInstance;
    }

    // 实现 SalesFormatter 接口的 formatSales 方法
    @Override
    public String formatSales(Sales sales) {
        StringBuilder output = new StringBuilder();

        // 开始HTML
        output.append("\n<html>\n");
        output.append(" <body>\n");
        output.append("  <center><h2>Orders</h2></center>\n");

        Iterator<Order> orderIterator = sales.iterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            output.append("  <hr>\n");

            // 输出订单总成本
            output.append("  <h4>Total = ").append(order.getTotalCost()).append("</h4>\n");

            Iterator<OrderItem> itemIterator = order.iterator();
            while (itemIterator.hasNext()) {
                OrderItem item = itemIterator.next();
                Product product = item.getProduct();

                // 输出每个产品的详细信息
                output.append("   <p>\n");
                output.append("    <b>code:</b> ").append(product.getCode()).append("<br>\n");
                output.append("    <b>quantity:</b> ").append(item.getQuantity()).append("<br>\n");
                output.append("    <b>price:</b> ").append(product.getPrice()).append("\n");
                output.append("   </p>\n");
            }
        }

        // 结束HTML
        output.append(" </body>\n");
        output.append("</html>");

        return output.toString();
    }
}