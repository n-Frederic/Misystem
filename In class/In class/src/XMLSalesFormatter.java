import java.util.Iterator;

public class XMLSalesFormatter implements SalesFormatter {

    // 静态变量，保存单例实例
    private static XMLSalesFormatter singletonInstance = new XMLSalesFormatter();

    // 私有构造函数
    private XMLSalesFormatter() {

    }

    // 静态方法，获取单例实例
    public static XMLSalesFormatter getSingletonInstance() {
        return singletonInstance;
    }

    // 实现 SalesFormatter 接口的 formatSales 方法
    @Override
    public String formatSales(Sales sales) {
        StringBuilder output = new StringBuilder();

        // 开始XML
        output.append("\n<Sales>\n");

        for (Order order : sales) {
            // 输出每个订单
            output.append(" <Order total=\"").append(order.getTotalCost()).append("\">\n");

            for (OrderItem item : order) {
                Product product = item.getProduct();
                // 输出每个订单项
                output.append("  <OrderItem quantity=\"").append(item.getQuantity())
                        .append("\" price=\"").append(product.getPrice())
                        .append("\">").append(product.getCode()).append("</OrderItem>\n");
            }

            // 结束当前订单
            output.append("</Order>\n");
        }

        // 结束XML
        output.append("</Sales>");

        return output.toString();
    }
}
