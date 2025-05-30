package M14.Ex1;

public class TestThreeThreads {
    public static void main(String[] args) {
        PrintMe printMe = new PrintMe();
        //创建三个线程
        Thread t1 = new Thread(new PrintMe());
        Thread t2 = new Thread(new PrintMe());
        Thread t3 = new Thread(new PrintMe());
        //分别命名
        t1.setName("我是第一个线程");
        t2.setName("我是第二个线程");
        t3.setName("我是第三个线程");
        //启动
        t1.start();
        t2.start();
        t3.start();

    }
}
