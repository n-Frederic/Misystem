package com.auggie.student_server.service;


import com.auggie.student_server.entity.CourseDTO;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamMapper examMapper;
public List<Exam> findExam(String sid, String term){

    List<Exam> list = examMapper.findExam(sid,term);
    return list;
}
    public List<Exam> findTeacherExam(String tid, String term){

        List<Exam> list = examMapper.findTeacherExam(tid,term);
        return list;
    }
    public List<Exam> findAllExam(){

        List<Exam> list = examMapper.findAllExam();
        return list;
    }


}
