package com.auggie.student_server.controller;

import com.auggie.student_server.entity.CTinfo;
import com.auggie.student_server.entity.ScInfo;
import com.auggie.student_server.entity.StudentCourse;
import com.auggie.student_server.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/SC")
public class SCcontroller {
    @Autowired
    private StudentCourseService studentCourseService;

    @PostMapping("/save")
    public String save(@RequestBody StudentCourse studentCourse) {
        if (studentCourseService.isSCExist(studentCourse)) {
            return "禁止重复选课";
        }
        System.out.println("正在保存 sc 记录：" + studentCourse);
        return studentCourseService.save(studentCourse) ? "选课成功" : "选课失败，联系管理员";
    }


    @GetMapping("/findBySid/{sid}/{term}")
    public List<CTinfo> findBySid(@PathVariable Integer sid, @PathVariable String term) {
        return studentCourseService.findBySid(sid, term);
    }


    @GetMapping("/findAllTerm")
    public List<String> findAllTerm() {
        return studentCourseService.findAllTerm();
    }

    @PostMapping("/deleteBySCT")
    public boolean deleteBySC(@RequestBody StudentCourse studentCourse) {
        System.out.println("正在删除 sc 记录：" + studentCourse);
        return studentCourseService.deleteBySC(studentCourse);
    }

    @PostMapping("/findBySearch")
    public List<ScInfo> findBySearch(@RequestBody Map<String, String> map) {
        return studentCourseService.findBySearch(map);
    }

    @GetMapping("/findById/{sid}/{cno}/{tid}/{term}")
    public ScInfo findById(@PathVariable Integer sid,
                           @PathVariable Integer cno,
                           @PathVariable Integer tid,
                           @PathVariable String term) {
        return studentCourseService.findByIdWithTerm(sid, cno, tid, term);
    }

    @GetMapping("/updateById/{sid}/{cno}/{term}/{grade}")
    public boolean updateById(@PathVariable Integer sid,
                              @PathVariable Integer cno,

                              @PathVariable String term,
                              @PathVariable Integer grade) {
        System.out.println("sid+"+sid+"cno"+cno+"term"+term+"grade"+grade);
        return studentCourseService.updateById(sid, cno,  term, grade);
    }
    @GetMapping("/findsc/{cno}")
    public boolean findByCno(@PathVariable Integer cno){
        System.out.println("wuwuuwuwuwuw");
        return studentCourseService.findsc(cno);
    }

    @GetMapping("/deleteById/{sid}/{cid}/{tid}/{term}")
    public boolean deleteById(@PathVariable Integer sid,
                              @PathVariable Integer cno,

                              @PathVariable String term) {
        return studentCourseService.deleteById(sid, cno, term);
    }



}
