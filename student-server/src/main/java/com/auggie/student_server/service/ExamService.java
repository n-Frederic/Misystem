package com.auggie.student_server.service;


import com.auggie.student_server.entity.CourseDTO;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.mapper.ExamMapper;
import org.mybatis.logging.LoggerFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auggie.student_server.mapper.ExamMapper;

import java.sql.Date;
import java.util.List;


@Service
public class ExamService {
    @Autowired
    private ExamMapper examMapper;

public List<Exam> findExam(Integer sid, String term){

    List<Exam> list = examMapper.findExam(sid,term);
    System.out.println("sid:"+sid+" "+"term"+term);
    System.out.println(list);
    return list;
}
    public List<Exam> findTeacherExam(Integer tid, String term){

        List<Exam> list = examMapper.findTeacherExam(tid,term);
        return list;
    }
    public List<Exam> findAllExam(){

        List<Exam> list = examMapper.findAllExam();
        return list;
    }
    public boolean insertExam(Integer cno, java.util.Date day, Integer period, Integer durationMin, Float grade, String location, String req) {


        java.sql.Date sqlDate = null;
        if (day != null) {
            sqlDate = new java.sql.Date(day.getTime());
        }


        boolean result = examMapper.insertExam(cno, sqlDate, period, durationMin, grade, location, req);

        return result;
    }


}
