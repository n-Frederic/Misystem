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
    private Integer day;
    private Integer period;
    private Integer capacity;
    private Integer selected;
    private String location;

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

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getDay() {
        return day;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDay(Integer day) {

    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPeriod() {
        return period;
    }

    public String getLocation() {
        return location;
    }

    public Integer getSelected() {
        return selected;
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
