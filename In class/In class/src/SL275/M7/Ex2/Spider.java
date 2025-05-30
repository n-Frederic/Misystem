package SL275.M7.Ex2;

public class Spider extends Animal {
    // a default constructor that calls the superclass constructor to specify that
    // all spiders have eight legs.
    public Spider() {
        super(8);
    }

    //Implement the eat method.
    public void eat() {
        System.out.println("The spider is eating...");
    }
}
