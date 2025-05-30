package M14.Ex3;

public class PrintJob {
    private String jobName;
    private int pages;

    public PrintJob(String jobName, int pages) {
        this.jobName = jobName;
        this.pages = pages;
    }

    public String getJobName() {
        return jobName;
    }

    public int getPages() {
        return pages;
    }

    @Override
    public String toString() {
        return " name:" + jobName + " pages: " + pages;
    }
}
