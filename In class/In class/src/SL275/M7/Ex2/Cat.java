package SL275.M7.Ex2;

public class Cat extends Animal implements Pet{

    private String name;

    // a constructor that takes one String parameter that specifies the cat's name.
    // also call the superclass constructor to specify that all cats have four legs.
    public Cat(String name){
        super(4);
        this.name=name;
    }

    //无参的构造函数，传一个空字符串
    public Cat(){
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

    //Implement the eat method.
    public void eat(){
        System.out.println("The cat is eating...");
    }

    //Implement the Pet interface methods.
    public void play() {
        System.out.println("The cat is playing...");
    }
}
