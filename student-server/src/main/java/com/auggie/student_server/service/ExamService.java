package com.auggie.student_server.service;


import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.mapper.ExamMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    @Autowired
    private ExamMapper examMapper;
public List<Exam> findExam(){
    return examMapper.findExam();
}

}
