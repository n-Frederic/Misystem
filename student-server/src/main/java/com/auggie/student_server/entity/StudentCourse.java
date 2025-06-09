package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("StudentCourse")
public class StudentCourse {
    private Integer sid;
    private Integer cno;
    private Float dailyGrade;

    private String term;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public Float getDailyGrade() {
        return dailyGrade;
    }

    public void setDailyGrade(Float dailyGrade) {
        this.dailyGrade = dailyGrade;
    }


    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
