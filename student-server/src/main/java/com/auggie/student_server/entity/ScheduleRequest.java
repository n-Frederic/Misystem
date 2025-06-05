package com.auggie.student_server.entity;

public class ScheduleRequest {
    private String sid;
    private String term;

    // 必须要有 getter/setter，Spring 才能反序列化 JSON

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
