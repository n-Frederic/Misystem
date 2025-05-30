package Thread;
public class Producer extends Thread {
    private MyStack stack;

    public Producer(MyStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        for (char c = 'A'; c <= 'Z'; c++) {
            try {
                stack.push(c);
                System.out.println("Producer pushed: " + c);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
