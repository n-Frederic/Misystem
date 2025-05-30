package SL275.M7.Ex2;

abstract class Animal {
    private int legs;

    //构造方法
    protected Animal(int legs) {
        this.legs = legs;
    }

    //打印动物如何走路
    public void walk() {
        System.out.println("walks on " + legs + " legs");
    }

    //抽象方法
    abstract void eat();
}
