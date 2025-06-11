package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Exam;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface ExamMapper {
    List<Exam>findExam();

}
