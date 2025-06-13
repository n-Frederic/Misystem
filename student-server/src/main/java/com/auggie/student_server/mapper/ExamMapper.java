package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
@Mapper
public interface ExamMapper {
    List<Exam>findExam(@Param("sid") Integer sid, @Param("term") String term);
    List<Exam>findTeacherExam(@Param("tid") Integer tid, @Param("term") String term);
    List<Exam>findAllExam();
    boolean deleteById(@Param("cno") Integer cno);
    boolean deletegrade(@Param("sid") Integer sid,@Param("cno") Integer cno);

    boolean insertExam(@Param("cno") Integer cno, @Param("day")  java.sql.Date day, @Param("period") Integer period, @Param("durationMin") Integer durationMin, @Param("grade") Float grade, @Param("location") String location, @Param("req") String req);
}
