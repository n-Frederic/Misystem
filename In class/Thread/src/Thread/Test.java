package Thread;

public class Test {
    public static void main(String[] args) {
        MyStack stack = new MyStack();
        Producer producer = new Producer(stack);
        Consumer consumer = new Consumer(stack);

        //启动线程
        producer.start();
        consumer.start();

        //主线程等待两个子线程完成
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}