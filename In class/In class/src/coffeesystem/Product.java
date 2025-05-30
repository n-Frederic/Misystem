package coffeesystem;

public class Product {

    /**
     * 产品的代码
     */
    private String code;

    /**
     * 产品的描述
     */
    private String description;

    /**
     * 产品的价格
     */
    private double price;

    /**
     * 获取产品的代码
     *
     * @return 产品的代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 构造一个新的产品对象
     *
     * @param initialCode        产品的初始代码
     * @param initialDescription 产品的初始描述
     * @param initialPrice       产品的初始价格
     */
    public Product(String initialCode, String initialDescription, double initialPrice) {
        this.code = initialCode;
        this.description = initialDescription;
        this.price = initialPrice;
    }

    /**
     * 获取产品的描述
     *
     * @return 产品的描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取产品的价格
     *
     * @return 产品的价格
     */
    public double getPrice() {
        return price;
    }

    /**
     * 重写Object类的equals方法。
     * 如果两个产品的代码相等，则它们被视为相等
     *
     * @param object 要比较的对象
     * @return 如果两个产品的代码相等，则返回true；否则返回false
     */
    @Override
    public boolean equals(Object object) {
        // 如果比较的是同一个对象，返回true
        if (this == object) {
            return true;
        }
        // 如果传入的对象是null，或者类型不匹配，返回false
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        // 向下转型
        Product product = (Product) object;
        // 比较code字段是否相等
        return code.equals(product.code);
    }

    /**
     * 重写Object类的hashCode方法
     *
     * @return 产品的哈希码
     */
    @Override
    public int hashCode() {
        return code.hashCode();
    }

    /**
     * 重写Object类的toString方法。
     * 返回包含产品代码、描述和价格的字符串表示形式。
     *
     * @return 产品的字符串表示形式
     */
    @Override
    public String toString() {
        return code + "_" + description + "_" + price;
    }
}