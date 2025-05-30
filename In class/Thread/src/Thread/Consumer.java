package Thread;

public class Consumer extends Thread {
    private MyStack stack;

    public Consumer(MyStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        char c;
        try {
            while ((c = stack.pop()) != 0) { // 假设栈为空时返回0（实际中不会返回0）
                System.out.println("Consumer popped: " + c);
                Thread.sleep(100); // 模拟耗时操作
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}