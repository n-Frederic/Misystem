package com.auggie.student_server.entity;

public class CourseDTO {
    private String cid;
    private String cname;
    private String tname;
    private String location;
    private int day;
    private int period;

    public CourseDTO(String cid, String cname, String tname, String location, int day, int period) {
        this.cid = cid;
        this.cname = cname;
        this.tname = tname;
        this.location = location;
        this.day = day;
        this.period = period;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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
