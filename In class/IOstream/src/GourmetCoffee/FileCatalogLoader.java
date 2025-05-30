import java.io.*;
import java.util.*;

public class FileCatalogLoader implements CatalogLoader {
    private Product readProduct(String line) throws DataFormatException {
        //元素由_分割
        StringTokenizer str = new StringTokenizer(line,"_");
        String str1 = str.nextToken();
        String code = str.nextToken();
        String description = str.nextToken();
        double price = Double.parseDouble(str.nextToken());
        return new Product(code,description,price);
    }

    // 辅助方法：检查一个字符串是否可以转换为double
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Coffee readCoffee(String line)
            throws DataFormatException {
        //元素由_分割
        StringTokenizer str = new StringTokenizer(line,"_");
        String name = str.nextToken();
        String code = str.nextToken();
        String description = str.nextToken();
        double price = Double.parseDouble(str.nextToken());
        String origin = str.nextToken();
        String roast = str.nextToken();
        String flavor = str.nextToken();
        String aroma = str.nextToken();
        String acidity = str.nextToken();
        String body = str.nextToken();
        return new Coffee(code,description,price,origin,roast,flavor,aroma,acidity,body);
    }

    private CoffeeBrewer readCoffeeBrewer(String line)
            throws DataFormatException {
        //元素由_分割
        StringTokenizer str = new StringTokenizer(line,"_");
        String name = str.nextToken();
        String code = str.nextToken();
        String description = str.nextToken();
        double price = Double.parseDouble(str.nextToken());
        String model = str.nextToken();
        String waterSupply = str.nextToken();
        int numberOfCups = Integer.parseInt(str.nextToken());
        return new CoffeeBrewer(code,description,price,model,waterSupply,numberOfCups);
    }

    // 公共方法：加载目录
    @Override
    public Catalog loadCatalog(String filename)
            throws FileNotFoundException, IOException, DataFormatException {

        Catalog catalog = new Catalog();

        try (BufferedReader reader = new BufferedReader
                (new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {

                //去除空白字符
                line = line.trim();

                // 根据行首的内容决定调用哪个方法
                if (line.startsWith("Product")) {
                    catalog.addProduct(readProduct(line));
                } else if (line.startsWith("Coffee")) {
                    catalog.addProduct(readCoffee(line));
                } else if (line.startsWith("Brewer")) {
                    catalog.addProduct(readCoffeeBrewer(line));
                } else {
                    // 未知的行类型，可以抛出异常或记录日志
                    throw new DataFormatException("Unknown line type: " + line);
                }
            }
        }
        return catalog;
    }
}