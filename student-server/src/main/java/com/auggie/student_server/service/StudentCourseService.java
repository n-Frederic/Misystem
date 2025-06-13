package com.auggie.student_server.service;

import com.auggie.student_server.entity.CTinfo;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.entity.ScInfo;
import com.auggie.student_server.entity.StudentCourse;
import com.auggie.student_server.mapper.StudentCourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class StudentCourseService {
    @Autowired
    private StudentCourseMapper studentCourseMapper;

    public List<CTinfo> findBySid(Integer sid, String term) {
        return studentCourseMapper.findByStudentId(sid,term);
    }

    public boolean findsc(Integer sid,Integer cno){
        return studentCourseMapper.findsc(sid,cno);
    }


    public List<String> findAllTerm(){
        return studentCourseMapper.findAllTerm();
    }



    public boolean isSCExist(StudentCourse studentCourse) {
        return studentCourseMapper.findBySC(studentCourse).size() != 0;
    }


    public boolean save(StudentCourse studentCourse) {
        return studentCourseMapper.insert(studentCourse);
    }


    public boolean deleteBySC(StudentCourse studentCourse) {
        return studentCourseMapper.deleteBySC(studentCourse);
    }


    public boolean deleteById(Integer sid, Integer cno, String  term) {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setSid(sid);
        studentCourse.setCno(cno);
        studentCourse.setTerm(term);
        return studentCourseMapper.deleteBySC(studentCourse);
    }

    public ScInfo findByIdWithTerm(Integer sid, Integer cno, Integer tid, String term) {
        return studentCourseMapper.findBySearch(
                sid, null, 0,
                cno, null, 0,
                tid, null, 0,
                null, null, term).get(0);
    }


    public boolean updateById(Integer sid, Integer cno, String term, Integer grade) {
        System.out.println("sid"+sid+"cno"+cno+"term"+term+"grade"+grade);
        return studentCourseMapper.updateById(sid, cno, term, grade);
    }
//tid到底要不要用

    public List<ScInfo> findBySearch(Map<String, String> map) {
        Integer sid = null, cid = null, tid = null;
        String sname = null, cname = null, tname = null, term = null;
        Integer sFuzzy = null, cFuzzy = null, tFuzzy = null;
        Integer lowBound = null, highBound = null;

        if (map.containsKey("cid")) {
            try {
                cid = Integer.parseInt(map.get("cid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("sid")) {
            try {
                sid = Integer.parseInt(map.get("sid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("tid")) {
            try {
                tid = Integer.parseInt(map.get("tid"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("sname")) {
            sname = map.get("sname");
        }
        if (map.containsKey("tname")) {
            tname = map.get("tname");
        }
        if (map.containsKey("cname")) {
            cname = map.get("cname");
        }
        if (map.containsKey("term")) {
            term = map.get("term");
        }
        if (map.containsKey("sFuzzy")) {
            sFuzzy = map.get("sFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("tFuzzy")) {
            tFuzzy = map.get("tFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("cFuzzy")) {
            cFuzzy = map.get("cFuzzy").equals("true") ? 1 : 0;
        }
        if (map.containsKey("lowBound")) {
            try {
                lowBound = Integer.parseInt(map.get("lowBound"));
            }
            catch (Exception e) {
            }
        }
        if (map.containsKey("highBound")) {
            try {
                highBound = Integer.valueOf(map.get("highBound"));
            }
            catch (Exception e) {
            }
        }

        System.out.println("SCT 查询：" + map);
        return studentCourseMapper.findBySearch(
                sid, sname, sFuzzy,
                cid, cname, cFuzzy,
                tid, tname, tFuzzy,
                lowBound, highBound, term);
    }
}
