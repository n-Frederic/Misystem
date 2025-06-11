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

    @GetMapping("/findExam/{sid}/{term}")
    public List<Exam> findExam(@PathVariable String sid,@PathVariable String term) {
        return examService.findExam(sid,term);
    }
    @GetMapping("/findTeacherExam/{tid}/{term}")
    public List<Exam> findTeacherExam(@PathVariable String tid,@PathVariable String term) {
        return examService.findTeacherExam(tid,term);
    }

    @GetMapping("/findAllExam")
    public List<Exam> findAllExam() {
        return examService.findAllExam();
    }

}