package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.classroomUseInfo;
import com.auggie.student_server.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @PostMapping("/findBySearch")
    public List<Course> findBySearch(@RequestBody Map<String, String> map) {
        return courseService.findBySearch(map);
    }

    @GetMapping("/findById/{cid}")
    public List<Course> findById(@PathVariable Integer cid) {
        return courseService.findBySearch(cid);
    }

    @PostMapping("/save")
    public boolean save(@RequestBody Course course) {
        System.out.println(course);
        return courseService.insertCourse(course);
    }

    @GetMapping("/deleteById/{cid}")
    public boolean deleteById(@PathVariable Integer cid) {
        System.out.println("正在删除课程 cid: " + cid);
        return courseService.deleteById(cid);
    }

    @PostMapping("/updateCourse")
    public boolean updateCourse(@RequestParam String cname,
                                @RequestParam Integer ccredit) {
        System.out.println("正在修改课程: " );

        return courseService.updateById(cname,ccredit);
    }

    @GetMapping("/getAllCourses")
    public List<classroomUseInfo> getAllCourses() {
        return courseService.getAllCourses();
    }

}
