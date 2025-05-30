package Student;

import java.io.*;
import java.util.*;

public class StudentSystem {
    private ArrayList<Student> students = new ArrayList<Student>();

    /**
     * print students information
     */
    public void display() {
        for (Iterator it = students.iterator(); it.hasNext(); ) {
            Student student = (Student) it.next();
            System.out.print(student.toString());
        }
    }

    /**
     *
     * @return students' average grade
     */
    public float calculateScoreAverage() {
        float sum = 0;
        int number = 0;
        for (Iterator it = students.iterator(); it.hasNext(); ) {
            Student student = (Student) it.next();
            sum = sum + student.getGrade();
            number++;
        }
        return sum / number;
    }

    /**
     * sort the ArrayList
     */
    public void sortArray() {
        GradeComp grade = new GradeComp();
        Collections.sort(students, grade);
    }

    /**
     * read the information from file
     */
    public void readFromFileToArrylist() {
        Scanner input = null;
        try {
            String str ;
            float grade;

            File file = new File("StudentInfo1.txt");
            input = new Scanner(file);
            while(input.hasNext()){
                Scanner input2 = new Scanner(input.next()).useDelimiter("_");
                str = input2.next();
                grade = input2.nextFloat();
                students.add(new Student(str,grade));
                input2.close();
            }

        }catch(FileNotFoundException e){
            System.out.println("the wrong information is "+ e.getMessage());
        }finally{
            if(input != null){
                input.close();
            }
        }
}

    /**
     * write information into the file
     */
    public void writeStudentFromFile(){
        try{
            File file = new File ("StudentInfo1.txt");
            OutputStream output = new DataOutputStream(new FileOutputStream(file));
            for(Iterator it = students.iterator(); it.hasNext();){
                Student student = (Student) it.next();
                String str = student.toString();
                output.write(str.getBytes());
                output.flush();
            }
            output.close();
        }catch(FileNotFoundException e){
            System.out.println("the wrong information is "+e.getMessage());
        }catch(IOException e1){
            System.out.println("the wrong information is "+e1.getMessage());
        }
    }

}
