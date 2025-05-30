package M15.Ex3;


import java.io.Serializable;

public class Product implements Serializable {
    private int quantity;
    private String name;


    public Product (String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return ("Product " + name + "Quantity : " + quantity);
    }
}
