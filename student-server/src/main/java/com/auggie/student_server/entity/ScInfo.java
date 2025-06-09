package com.auggie.student_server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Alias("ScInfo")
public class ScInfo {
    private Integer sid;
    private Integer cno;
    private Float dailyGrade;
    private Float finalGrade;


}
