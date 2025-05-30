package M14.Ex1;

public class PrintMe implements Runnable{
    public void run() {
        //循环十次
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName());
        }
        try {
            //休息
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
