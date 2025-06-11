package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface ExamMapper {
    List<Exam>findExam(@Param("sid") Integer sid, @Param("term") String term);
    List<Exam>findTeacherExam(@Param("tid") Integer tid, @Param("term") String term);
    List<Exam>findAllExam();

}
