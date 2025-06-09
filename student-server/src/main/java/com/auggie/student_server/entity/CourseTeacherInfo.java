package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("CourseTeacherInfo")
public class CourseTeacherInfo {
    private Integer cno;
    private Integer tid;
    private String cname;
    private String tname;
    private Integer ccredit;
    private Float grade;



    // Getter 和 Setter 方法
    public Integer getCno() { return cno; }
    public void setCno(Integer cno) { this.cno = cno; }



    public String getCname() { return cname; }
    public void setCname(String cname) { this.cname = cname; }

    public Integer getTid() { return tid; }
    public void setTid(Integer tid) { this.tid = tid; }

    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }


}
