package Date;
public class TestDate {
    public static void main(String[] args) {
        Date date = new Date(-1, 1, 1);
        date.setDate(2023, 100, 11);
        date.addOneDay();
        date.display();
    }
}