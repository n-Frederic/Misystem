package M14.Ex3;

public class Producer implements Runnable {
    private int sizeOfJobs;
    private int numberOfJobs;
    private int delayBetweenJobs;
    private String producerName;

    //构造方法
    public Producer(int sizeOfJobs, int numberOfJobs, int delayBetweenJobs, String producerName) {
        this.sizeOfJobs = sizeOfJobs;
        this.numberOfJobs = numberOfJobs;
        this.delayBetweenJobs = delayBetweenJobs;
        this.producerName = producerName;
    }

    @Override
    //添加任务
    public void run() {
        for (int i = 0; i < numberOfJobs; i++) {
            String jobName = producerName + i;
            int pages = sizeOfJobs;
            try {
                Printer.getInstance().addJob(new PrintJob(jobName, pages));
                //每个任务中间休息一下
                Thread.sleep(500);
            } catch (FullQueueException e) {
                System.out.println("Full Queue Exception");
            }catch (InterruptedException e){
                System.out.println("Interrupted");
            }
        }
    }
}
