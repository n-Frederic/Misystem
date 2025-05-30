package coffeesystem;

public class Coffee extends Product {

    /**
     * 咖啡的产地
     */
    private String origin;

    /**
     * 咖啡的烘焙程度
     */
    private String roast;

    /**
     * 咖啡的风味描述
     */
    private String flavor;

    /**
     * 咖啡的香气描述
     */
    private String aroma;

    /**
     * 咖啡的酸度描述
     */
    private String acidity;

    /**
     * 咖啡的口感（浓郁度）描述
     */
    private String body;

    /**
     * 构造一个新的咖啡对象
     *
     * @param initialCode        咖啡的代码
     * @param initialDescription 咖啡的描述信息
     * @param initialPrice       咖啡的价格
     * @param initialOrigin      咖啡的产地
     * @param initialRoast       咖啡的烘焙程度
     * @param initialFlavor      咖啡的风味描述
     * @param initialAroma       咖啡的香气描述
     * @param initialAcidity     咖啡的酸度描述
     * @param initialBody        咖啡的口感（浓郁度）描述
     */
    public Coffee(String initialCode, String initialDescription, double initialPrice,
                  String initialOrigin, String initialRoast, String initialFlavor,
                  String initialAroma, String initialAcidity, String initialBody) {
        super(initialCode, initialDescription, initialPrice);
        this.origin = initialOrigin;
        this.roast = initialRoast;
        this.flavor = initialFlavor;
        this.aroma = initialAroma;
        this.acidity = initialAcidity;
        this.body = initialBody;
    }

    /**
     * 获取咖啡的产地
     *
     * @return 咖啡的产地
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 获取咖啡的烘焙程度
     *
     * @return 咖啡的烘焙程度
     */
    public String getRoast() {
        return roast;
    }

    /**
     * 获取咖啡的风味描述
     *
     * @return 咖啡的风味描述
     */
    public String getFlavor() {
        return flavor;
    }

    /**
     * 获取咖啡的香气描述
     *
     * @return 咖啡的香气描述
     */
    public String getAroma() {
        return aroma;
    }

    /**
     * 获取咖啡的酸度描述
     *
     * @return 咖啡的酸度描述
     */
    public String getAcidity() {
        return acidity;
    }

    /**
     * 获取咖啡的口感（浓郁度）描述
     *
     * @return 咖啡的口感（浓郁度）描述
     */
    public String getBody() {
        return body;
    }

    /**
     * 返回包含咖啡所有信息的字符串表示形式。
     * 格式为：代码_描述_价格_产地_烘焙程度_风味_香气_酸度_口感
     *
     * @return 咖啡的字符串表示
     */
    @Override
    public String toString() {
        return getCode() + "_" + getDescription() + "_" + getPrice() + "_" +
                origin + "_" + roast + "_" + flavor + "_" +
                aroma + "_" + acidity + "_" + body;
    }
}