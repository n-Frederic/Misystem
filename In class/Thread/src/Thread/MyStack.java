package Thread;
import java.lang.*;

public class MyStack {
    private char[] stack = new char[5];
    private int top = -1;

    public synchronized void push(char c) throws InterruptedException{
        while (top == stack.length - 1) { //栈满，等待消费者消费
            wait();
        }
        stack[++top] = c;
        notifyAll(); //通知可能正在等待的消费者和生产者
    }
    public synchronized char pop() throws InterruptedException{
        while (top == -1) { //栈空，等待生产者生产
            wait();
        }
        char c = stack[top--];
        notifyAll(); //通知可能正在等待的消费者和生产者
        return c;
    }
}
