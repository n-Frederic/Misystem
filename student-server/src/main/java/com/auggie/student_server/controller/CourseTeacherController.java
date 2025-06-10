package com.auggie.student_server.controller;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/courseTeacher")
public class CourseTeacherController {

    @Autowired
    private CourseTeacherService courseTeacherService;

    /**
     * 插入新的开课记录
     * @param cno 开课编号
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 是否插入成功
     */
    @GetMapping("/insert/{cno}/{cid}/{tid}/{term}")
    public boolean insert(@PathVariable Integer cno,
                          @PathVariable Integer cid,
                          @PathVariable Integer tid,
                          @PathVariable String term) {
        // 检查是否已存在相同的开课记录
        if (courseTeacherService.findByCno(cno) != null) {
            return false; // cno已存在
        }

        // 检查是否已有相同的课程-教师-学期组合
        if (courseTeacherService.findBySearch(cid, tid, term).size() != 0) {
            return false; // 已存在相同的课程-教师-学期组合
        }

        return courseTeacherService.insertCourseTeacher(cno, cid, tid, term);
    }

    /**
     * 简化版插入（自动生成cno）
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 是否插入成功
     */
    @GetMapping("/insertAuto/{cid}/{tid}/{term}")
    public boolean insertAuto(@PathVariable Integer cid,
                              @PathVariable Integer tid,
                              @PathVariable String term) {
        // 检查是否已有相同的课程-教师-学期组合
        if (courseTeacherService.findBySearch(cid, tid, term).size() != 0) {
            return false;
        }

        // 自动生成cno并插入
        return courseTeacherService.insertCourseTeacherAuto(cid, tid, term);
    }

    /**
     * 根据开课编号查询开课信息
     * @param cno 开课编号
     * @return 开课信息
     */
    @GetMapping("/findByCno/{cno}")
    public CourseTeacher findByCno(@PathVariable Integer cno) {
        return courseTeacherService.findByCno(cno);
    }

    /**
     * 查询教师在指定学期的课程
     * @param tid 教师ID
     * @param term 学期
     * @return 课程列表
     */
    @GetMapping("/findMyCourse/{tid}/{term}")
    public List<CourseTeacherInfo> findMyCourse(@PathVariable Integer tid, @PathVariable String term) {
        System.out.println("查询教师课程：" + tid + " " + term);
        return courseTeacherService.findMyCourse(tid, term);
    }

    /**
     * 查询教师的开课记录（包含cno信息）
     * @param tid 教师ID
     * @param term 学期
     * @return 开课记录列表
     */
    @GetMapping("/findMyCourseTeacher/{tid}/{term}")
    public List<CourseTeacher> findMyCourseTeacher(@PathVariable Integer tid, @PathVariable String term) {
        return courseTeacherService.findMyCourseTeacher(tid, term);
    }

    /**
     * 查询开课信息详情
     * @param map 查询条件
     * @return 开课信息详情列表
     */
    @PostMapping("/findCourseTeacherInfo")
    public List<CourseTeacherInfo> findCourseTeacherInfo(@RequestBody Map<String, String> map) {
        return courseTeacherService.findCourseTeacherInfo(map);
    }

    /**
     * 根据开课编号删除开课记录
     * @param cno 开课编号
     * @return 是否删除成功
     */
    @DeleteMapping("/deleteByCno/{cno}")
    public boolean deleteByCno(@PathVariable Integer cno) {
        return courseTeacherService.deleteByCno(cno);
    }

    /**
     * 根据CourseTeacher对象删除开课记录
     * @param courseTeacher 开课记录对象
     * @return 是否删除成功
     */
    @PostMapping("/deleteById")
    public boolean deleteById(@RequestBody CourseTeacher courseTeacher) {
        // 如果传入的是cno，直接根据cno删除
        if (courseTeacher.getCno() != null) {
            return courseTeacherService.deleteByCno(courseTeacher.getCno());
        }
        // 否则根据其他字段组合删除
        return courseTeacherService.deleteById(courseTeacher);
    }

    /**
     * 更新开课记录
     * @param courseTeacher 开课记录对象
     * @return 是否更新成功
     */
    @PostMapping("/update")
    public boolean update(@RequestBody CourseTeacher courseTeacher) {
        return courseTeacherService.updateCourseTeacher(courseTeacher);
    }

    /**
     * 查询所有开课记录
     * @return 所有开课记录列表
     */
    @GetMapping("/findAll")
    public List<CourseTeacher> findAll() {
        return courseTeacherService.findAll();
    }

    /**
     * 根据学期查询开课记录
     * @param term 学期
     * @return 开课记录列表
     */
    @GetMapping("/findByTerm/{term}")
    public List<CourseTeacher> findByTerm(@PathVariable String term) {
        return courseTeacherService.findByTerm(term);
    }
}