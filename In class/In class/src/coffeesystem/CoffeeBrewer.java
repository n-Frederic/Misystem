package coffeesystem;

public class CoffeeBrewer extends Product {

    /**
     * 冲泡机的型号
     */
    private String model;

    /**
     * 冲泡机的供水方式
     */
    private String waterSupply;

    /**
     * 冲泡机一次能冲泡的最大杯数
     */
    private int numberOfCups;

    /**
     * 构造一个新的冲泡机对象
     *
     * @param initialCode 冲泡机的唯一代码
     * @param initialDescription 冲泡机的描述信息
     * @param initialPrice 冲泡机的价格
     * @param initialModel 冲泡机的型号
     * @param initialWaterSupply 冲泡机的供水方式
     * @param initialNumberOfCups 冲泡机一次能冲泡的最大杯数
     */
    public CoffeeBrewer(String initialCode, String initialDescription, double initialPrice,
                        String initialModel, String initialWaterSupply, int initialNumberOfCups) {
        super(initialCode, initialDescription, initialPrice);
        this.model = initialModel;
        this.waterSupply = initialWaterSupply;
        this.numberOfCups = initialNumberOfCups;
    }

    /**
     * 获取冲泡机的型号
     *
     * @return 冲泡机的型号
     */
    public String getModel() {
        return model;
    }

    /**
     * 获取冲泡机的供水方式
     *
     * @return 冲泡机的供水方式
     */
    public String getWaterSupply() {
        return waterSupply;
    }

    /**
     * 获取冲泡机一次能冲泡的最大杯数
     *
     * @return 冲泡机一次能冲泡的最大杯数
     */
    public int getNumberOfCups() {
        return numberOfCups;
    }

    /**
     * 返回包含冲泡机所有信息的字符串表示形式
     * 格式为：代码_描述_价格_型号_供水方式_最大杯数
     *
     * @return 冲泡机的字符串表示
     */
    @Override
    public String toString() {
        return getCode() + "_" + getDescription() + "_" + getPrice() + "_" +
                model + "_" + waterSupply + "_" + numberOfCups;
    }
}