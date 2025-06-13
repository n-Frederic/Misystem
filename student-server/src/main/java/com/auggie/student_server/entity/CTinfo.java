package com.auggie.student_server.entity;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("CTinfo")
public class CTinfo {
    private Integer cno;

    private String cname;
    private Integer tid;
private String tname;

    private Integer ccredit;
    private Float daily_grade;



    // Getter 和 Setter 方法
    public Integer getCno() { return cno; }
    public void setCno(Integer cno) { this.cno = cno; }


    public Integer getCredit() {
        return ccredit;
    }

    public void setCredit(Integer ccredit) {
        this.ccredit = ccredit;
    }

    public Float getDaily_grade() {
        return daily_grade;
    }

    public void setDaily_grade(Float daily_grade) {
        this.daily_grade = daily_grade;
    }

    public String getCname() { return cname; }
    public void setCname(String cname) { this.cname = cname; }

    public Integer getTid() { return tid; }
    public void setTid(Integer tid) { this.tid = tid; }

    public String getTname() { return tname; }
    public void setTname(String tname) { this.tname = tname; }


}
