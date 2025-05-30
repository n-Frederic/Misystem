import java.util.*;

public class Catalog implements Iterable<Product>{
    List<Product> products;

    //返回一个迭代器，用于遍历集合
    @Override
    public Iterator<Product> iterator() {
        return products.iterator();
    }

    //创建商品集合，初始化为空
    public Catalog() {
        this.products = new ArrayList<>();
    }

    //向集合中增加一个特定的产品
    public void addProduct(Product product) {
        products.add(product);
    }

    //通过序号检索产品
    public Product getProduct(String code){
        for(Product product : products){
            if(product.getCode().equals(code)){
                return product;
            }
        }
        return null;
    }

    //获取产品的总数
    public int getNumberOfProducts(){
        return products.size();
    }
}
