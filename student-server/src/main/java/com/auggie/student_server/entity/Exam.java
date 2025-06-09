package com.auggie.student_server.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Alias("Exam")
public class Exam {
    private Integer cno;
    private Date day;
    private Integer period;
    private Integer durationMin;
    private Float grade;
    private String location;
    private String Req;

}
