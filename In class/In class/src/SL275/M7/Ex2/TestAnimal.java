package SL275.M7.Ex2;

public class TestAnimal {
    public static void main(String[] args) {
        Fish d = new Fish();
        Cat c = new Cat("Fluffy");
        Animal a = new Fish();
        Animal e = new Spider();
        Pet p = new Cat();

        // Call methods in each object
        System.out.println(d.getName());
        d.eat();
        d.walk();
        System.out.println(c.getName());
        c.eat();
        c.play();

        // Casting objects
        if (a instanceof Fish) {
            Fish fish = (Fish) a;
            fish.eat();
        }

        if (e instanceof Spider) {
            Spider spider = (Spider) e;
            spider.eat();
        }

        p.setName("meow");
        System.out.println(p.getName());
        if (p instanceof Cat) {
            Cat cat = (Cat) p;
            cat.setName("");
            cat.play();
        }

        // Using polymorphism
        a.walk();//调用fish中的walk方法
        e.walk();//调用animal中的walk方法

        // Using super to call super class methods
        c.walk();

    }
}