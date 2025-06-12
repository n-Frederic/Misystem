package com.auggie.student_server.controller;


import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.Exam;
import com.auggie.student_server.service.CourseTeacherService;
import com.auggie.student_server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @GetMapping("/deleteById/{cno}")
    public boolean deleteById(@PathVariable Integer cno) {  // 或 Integer cno，取决于cno的类型
        try {
            // 调用service层删除逻辑
            boolean result = examService.deleteById(cno);
            System.out.println("cno"+cno);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @GetMapping("/findAllExam")
    public List<Exam> findAllExam() {
        return examService.findAllExam();
    }


    @PostMapping("/add")
    public Map<String, Object> addExam(@RequestBody Map<String, Object> examData) {
        Map<String, Object> result = new HashMap<>();

        try {
            System.out.println(examData);
            Integer cno = (Integer) examData.get("cno");
            String dayStr = (String) examData.get("day");
            Integer period = (Integer) examData.get("period");
            Integer durationMin = (Integer) examData.get("durationMin");
            Float grade = examData.get("grade") instanceof Integer ? ((Integer) examData.get("grade")).floatValue() : (Float) examData.get("grade");
            String location = (String) examData.get("location");
            String req = (String) examData.get("req");

            if (cno == null) {
                System.out.println("cno is null");
                result.put("success", false);
                result.put("message", "课程编号不能为空");
                return result;
            }
            if (dayStr == null) {
                result.put("success", false);
                result.put("message", "考试日期不能为空");
                return result;
            }
            if (period == null) {
                result.put("success", false);
                result.put("message", "考试时段不能为空");
                return result;
            }
            if (durationMin == null) {
                result.put("success", false);
                result.put("message", "考试时长不能为空");
                return result;
            }
            if (grade == null || grade < 0 || grade > 100) {
                result.put("success", false);
                result.put("message", "考试总分必须在0-100之间");
                return result;
            }
            if (req == null || req.isEmpty()) {
                result.put("success", false);
                result.put("message", "考试要求不能为空");
                return result;
            }

            java.util.Date day;
            try {
                day = new SimpleDateFormat("yyyy-MM-dd").parse(dayStr);
            } catch (ParseException e) {
                System.out.println("invalid date");
                result.put("success", false);
                result.put("message", "考试日期格式无效");
                return result;
            }

            boolean success = examService.insertExam(cno, day, period, durationMin, grade, location, req);
            result.put("success", success);
            result.put("message", success ? "考试添加成功" : "考试添加失败，该课程可能已存在考试");

        } catch (IllegalArgumentException e) {

            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (DataAccessException e) {

            result.put("success", false);
            result.put("message", "数据库错误：" + e.getMessage());
        } catch (Exception e) {

            result.put("success", false);
            result.put("message", "系统异常：" + e.getMessage());
        }
        return result;
    }


}