package com.auggie.student_server.entity;

public class CourseDTO {
    private Integer cno;
    private String cname;
    private String tname;
    private String location;
    private int day;
    private int period;

    public CourseDTO(Integer cno, String cname, String tname, String location, int day, int period) {
        this.cno = cno;
        this.cname = cname;
        this.tname = tname;
        this.location = location;
        this.day = day;
        this.period = period;
    }

    public Integer getcno() {
        return cno;
    }

    public void setcno(Integer cno) {
        this.cno = cno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
