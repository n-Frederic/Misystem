package com.auggie.student_server.controller;


import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.service.CourseTeacherService;
import com.auggie.student_server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/findExam")
    public List<Exam> findExam() {
        return examService.findExam();
    }

}