package SL275.M7.Ex2;

public class Fish extends Animal implements Pet{
    private String name;

    // a constructor that calls the superclass constructor to specify that
    // all fish have no legs.
    public Fish(String name){
        super(0);
    }

    //无参的构造函数
    public Fish(){
        this("");
    }

    //设置姓名
    public void setName(String name) {
        this.name = name;
    }

    //获取姓名
    public String getName(){
        return name;
    }

    //实现鱼吃东西的方法
    public void eat(){
        System.out.println("The fish is eating...");
    }

    //重写walk方法，鱼游泳而不走路
    public void walk() {
        System.out.println("The fish swim");
    }

    //鱼玩
    public void play() {
        System.out.println("The fish is playing...");
    }
}
