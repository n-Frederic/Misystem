package book;

public class Book {
    private String name;
    private Author[] authors;
    private double price;
    private int qty = 0;

    //重写toString方法（没写完
    public String toString() {
        String atif = "";
        for (int i = 0; i < authors.length; i++) {
            atif += authors[i].toString();
        }
        return "Book[name=" + name + "， \nauthors=" + atif +
                "， \nprice=" + price + "， qty=" + qty;
    }

    //构造方法1
    public Book(String name, Author[] authors, double price) {
        this.authors = authors;
        this.name = name;
        this.price = price;
        this.qty = 0;
    }

    //构造方法2
    public Book(String name, Author[] authors, int qty) {
        this.authors = authors;
        this.name = name;
        this.qty = qty;
        this.price = 0.0;
    }

    //获取作者名字
    public String[] getAuthorNames() {
        String[] anames = new String[authors.length];
        for (int i = 0; i < authors.length; i++) {
            anames[i] = authors[i].name;
        }
        return anames;
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public Author[] getAuthors() {
        return authors;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
