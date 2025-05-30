package Student;

public class Student {
    private String name;
    private float grade;

    /**
     *
     * @param name 姓名
     * @param grade 成绩
     */
    public Student(String name, float grade){
        this.name = name;
        this.grade = grade;
    }

    /**
     *
     * @return 姓名
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return 成绩
     */
    public float getGrade(){
        return grade;
    }

    /**
     *
     * @return 信息
     */
    public String toString(){
        return name + "\t"+ grade+"\n" ;
    }


}
