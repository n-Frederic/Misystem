package com.auggie.student_server.controller;

import com.auggie.student_server.entity.CourseDTO;
import com.auggie.student_server.entity.ScheduleRequest;
import com.auggie.student_server.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/Schedule")
@CrossOrigin("*")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/getStudentSchedule")
    public List<CourseDTO> getStudentSchedule(@RequestBody ScheduleRequest request) {
        String sid = request.getSid();
        String term = request.getTerm();

        // 示例逻辑：根据学号和学期查找课程
        List<CourseDTO> courseList = scheduleService.findCoursesByStudentAndTerm(sid, term);

        return courseList; // 自动转为 JSON 数组返回
    }
}
