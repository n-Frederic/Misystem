package coffeesystem;

public class OrderItem {

    /**
     * 产品对象
     */
    private Product product;

    /**
     * 产品的数量
     */
    private int quantity;

    /**
     * 创建一个新的订单
     *
     * @param initialProduct 订单中的产品对象
     * @param initialQuantity 订单中产品的数量
     */
    public OrderItem(Product initialProduct, int initialQuantity) {
        this.product = initialProduct;
        this.quantity = initialQuantity;
    }

    /**
     * 获取订单中的产品
     *
     * @return 订单中的产品对象
     */
    public Product getProduct() {
        return product;
    }

    /**
     * 获取订单中产品的数量。
     *
     * @return 订单中产品的数量
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 设置订单中产品的数量。
     *
     * @param newQuantity 新的产品数量
     */
    public void setQuantity(int newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * 计算订单的总价值，即产品数量乘以产品价格。
     *
     * @return 订单的总价值
     */
    public double getValue() {
        return quantity * product.getPrice();
    }

    /**
     * 返回包含订单信息的字符串表示形式
     *
     * @return 订单的字符串表示
     */
    @Override
    public String toString() {
        return quantity + " " + product.getCode() + " " + product.getPrice();
    }
}