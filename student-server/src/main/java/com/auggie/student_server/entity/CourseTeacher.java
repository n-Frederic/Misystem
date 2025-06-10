package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("CourseTeacher")
public class CourseTeacher {
    private Integer cno;
    private Integer cid;
    private Integer tid;
    private String term;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public Integer getCid() {
        return cid;
    }

    public Integer getTid() {
        return tid;
    }

    public Integer getCno() {
        return cno;
    }
}
