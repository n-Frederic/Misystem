package Student;

import java.util.*;
public class GradeComp implements Comparator<Student>{
    public int compare(Student student1, Student student2){
        if(student1.getGrade() == student2.getGrade()){
            return 0;
        }
        else if(student1.getGrade() > student2.getGrade()){
            return 1;
        }
        else if(student1.getGrade() < student2.getGrade()){
            return -1;
        }
        return 0;
    }
}
