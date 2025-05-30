package M14.Ex3;

public class TestPrinter {
    public static void main(String[] args) {

        //创建一个printer实例
        Printer printer = Printer.getInstance();

        //创建一个线程，并将优先级设为最高
        Thread a = new Thread(printer, "print manager");
        a.setPriority(Thread.MAX_PRIORITY);
        a.start();

        //创建三个producer
        Producer p1 = new Producer(5, 1, 2, "第一个");
        Producer p2 = new Producer(15, 1, 6, "第二个");
        Producer p3 = new Producer(25, 1,10, "第三个");

        //创建并启动线程
        Thread t1 = new Thread(p1, "第一个");
        Thread t2 = new Thread(p2, "第二个");
        Thread t3 = new Thread(p3, "第三个");
        t1.start();
        t2.start();
        t3.start();

        //等待每个producer线程完成
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //结束线程
        printer.halt();
        System.out.println("All jobs have been processed and the printer has been halted.");

    }
}
