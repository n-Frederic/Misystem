package com.auggie.student_server.service;


import com.auggie.student_server.entity.CourseDTO;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.mapper.ExamMapper;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
