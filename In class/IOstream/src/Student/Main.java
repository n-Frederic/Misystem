package Student;

public class Main {
    public static void main(String[] args) {
        StudentSystem ss = new StudentSystem();
        ss.readFromFileToArrylist();
        ss.display();
        System.out.println(ss.calculateScoreAverage());
        ss.sortArray();
        ss.display();
        ss.writeStudentFromFile();
    }
}