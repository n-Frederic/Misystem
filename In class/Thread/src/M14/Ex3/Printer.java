package M14.Ex3;

public class Printer implements Runnable {
    private Queue printQueue;
    private boolean stateIsRunning = true;
    //使用volatile确保跨线程的可见性
    private static volatile Printer instance;

    //私有构造方法
    private Printer() {
        printQueue = new CircularQueue(5);
    }

    //获取单例
    public static Printer getInstance() {
        if (instance == null) {
            //同步块确保线程安全
            synchronized (Printer.class) {
                if (instance == null) {
                    //创建单例对象
                    instance = new Printer();
                }
            }
        }
        return instance;
    }

    //终止线程
    public synchronized void halt() {
        this.stateIsRunning = false;
    }

    //添加任务
    public synchronized void addJob(PrintJob job) throws FullQueueException {
        printQueue.addBack(job);
        this.notifyAll();
    }

    //获取任务
    private synchronized PrintJob getJob() throws EmptyQueueException {
        try {
            if (printQueue.isEmpty()) {
                this.wait();
                throw new EmptyQueueException(printQueue);
            }
        } catch (InterruptedException e) {
            throw new EmptyQueueException(printQueue);
        }
        //强制类型转化
        PrintJob job = (PrintJob) printQueue.removeFront();
        return job;
    }

    @Override
    public void run(){
        //获取任务并打印，一开始没有就等待
        while (stateIsRunning) {
            try {
                PrintJob job = getJob();
                System.out.println("The job is starting" + job);
            } catch (EmptyQueueException e) {
                System.out.println("Waiting on a job");
            } catch (FullQueueException fqe) {
                System.out.println("The queue is full");
                halt();
                stateIsRunning = false;
            }
        }
    }
}
