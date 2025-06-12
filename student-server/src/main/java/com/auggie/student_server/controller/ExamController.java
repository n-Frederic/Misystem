package com.auggie.student_server.controller;


import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.service.CourseTeacherService;
import com.auggie.student_server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @GetMapping("/findExam/{sid}/{term}")
    public List<Exam> findExam(@PathVariable Integer sid,@PathVariable String term) {
        System.out.println("controller: sid: "+sid+" "+"term "+term);
        return examService.findExam(sid,term);
    }
    @GetMapping("/findTeacherExam/{tid}/{term}")
    public List<Exam> findTeacherExam(@PathVariable Integer tid,@PathVariable String term) {
        List<Exam> exams = examService.findTeacherExam(tid,term);
        return exams;
    }

    @GetMapping("/findAllExam")
    public List<Exam> findAllExam() {
        return examService.findAllExam();
    }

    /**
     * 添加考试
     * @param exam 考试信息
     * @return 响应结果
     */
    @PostMapping("/add")
    public Map<String, Object> addExam(@RequestBody Exam exam) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 参数验证
            if (exam.getCno() == null) {
                result.put("success", false);
                result.put("message", "课程编号不能为空");
                return result;
            }

            boolean success = examService.insertExam(exam);
            result.put("success", success);

            if (success) {
                result.put("message", "考试添加成功");
            } else {
                result.put("message", "考试添加失败，该课程可能已存在考试");
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "系统异常：" + e.getMessage());
        }

        return result;
    }


}