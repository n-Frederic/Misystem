package com.auggie.student_server.service;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import com.auggie.student_server.mapper.CourseTeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CourseTeacherService {

    @Autowired
    private CourseTeacherMapper courseTeacherMapper;

    /**
     * 插入开课记录（指定cno）
     * @param cno 开课编号
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 是否插入成功
     */
    public boolean insertCourseTeacher(Integer cno, Integer cid, Integer tid, String term) {
        return courseTeacherMapper.insertCourseTeacher(cno, cid, tid, term);
    }

    /**
     * 插入开课记录（自动生成cno）
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 是否插入成功
     */
    public boolean insertCourseTeacherAuto(Integer cid, Integer tid, String term) {
        // 生成新的cno（可以根据业务规则生成，这里使用简单的时间戳方式）
        Integer cno = generateNewCno();
        return courseTeacherMapper.insertCourseTeacher(cno, cid, tid, term);
    }

    /**
     * 保持向后兼容的插入方法
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 是否插入成功
     */
    public boolean insertCourseTeacher(Integer cid, Integer tid, String term) {
        return insertCourseTeacherAuto(cid, tid, term);
    }

    /**
     * 根据开课编号查询开课记录
     * @param cno 开课编号
     * @return 开课记录
     */
    public CourseTeacher findByCno(Integer cno) {
        return courseTeacherMapper.findByCno(cno);
    }

    /**
     * 查询教师在指定学期的课程
     * @param tid 教师ID
     * @param term 学期
     * @return 课程列表
     */
    public List<Course> findMyCourse(Integer tid, String term) {
        return courseTeacherMapper.findMyCourse(tid, term);
    }

    /**
     * 查询教师的开课记录（包含cno信息）
     * @param tid 教师ID
     * @param term 学期
     * @return 开课记录列表
     */
    public List<CourseTeacher> findMyCourseTeacher(Integer tid, String term) {
        return courseTeacherMapper.findMyCourseTeacher(tid, term);
    }

    /**
     * 查询开课信息详情
     * @param map 查询条件
     * @return 开课信息详情列表
     */
    public List<CourseTeacherInfo> findCourseTeacherInfo(Map<String, String> map) {
        Integer tid = null, cid = null, cno = null;
        Integer tFuzzy = null, cFuzzy = null;
        String tname = null, cname = null;

        if (map.containsKey("tid")) {
            try {
                tid = Integer.parseInt(map.get("tid"));
            } catch (Exception e) {
            }
        }
        if (map.containsKey("cid")) {
            try {
                cid = Integer.parseInt(map.get("cid"));
            } catch (Exception e) {
            }
        }
        if (map.containsKey("tname")) {
            tname = map.get("tname");
        }
        if (map.containsKey("cname")) {
            cname = map.get("cname");
        }
        if (map.containsKey("tFuzzy")) {
            tFuzzy = (map.get("tFuzzy").equals("true")) ? 1 : 0;
        }
        if (map.containsKey("cFuzzy")) {
            cFuzzy = (map.get("cFuzzy").equals("true")) ? 1 : 0;
        }

        System.out.println("ct 模糊查询" + map);
        System.out.println(courseTeacherMapper.findCourseTeacherInfo(tid,cno, tname, tFuzzy, cid, cname, cFuzzy));
        return courseTeacherMapper.findCourseTeacherInfo(tid, cno,tname, tFuzzy, cid, cname, cFuzzy);
    }

    /**
     * 根据条件查询开课记录
     * @param cid 课程ID
     * @param tid 教师ID
     * @param term 学期
     * @return 开课记录列表
     */
    public List<CourseTeacher> findBySearch(Integer cid, Integer tid, String term) {
        return courseTeacherMapper.findBySearch(cid, tid, term);
    }

    /**
     * 根据Map条件查询开课记录
     * @param map 查询条件
     * @return 开课记录列表
     */
    public List<CourseTeacher> findBySearch(Map<String, String> map) {
        Integer cid = null;
        Integer tid = null;
        String term = null;

        if (map.containsKey("term")) {
            term = map.get("term");
        }
        if (map.containsKey("tid")) {
            try {
                tid = Integer.parseInt(map.get("tid"));
            } catch (Exception e) {
            }
        }
        if (map.containsKey("cid")) {
            try {
                cid = Integer.parseInt(map.get("cid"));
            } catch (Exception e) {
            }
        }

        System.out.println("开课表查询：" + map);
        return courseTeacherMapper.findBySearch(cid, tid, term);
    }

    /**
     * 根据开课编号删除开课记录
     * @param cno 开课编号
     * @return 是否删除成功
     */
    public boolean deleteByCno(Integer cno) {
        return courseTeacherMapper.deleteByCno(cno);
    }

    /**
     * 根据CourseTeacher对象删除开课记录
     * @param courseTeacher 开课记录对象
     * @return 是否删除成功
     */
    public boolean deleteById(CourseTeacher courseTeacher) {
        return courseTeacherMapper.deleteById(courseTeacher);
    }

    /**
     * 更新开课记录
     * @param courseTeacher 开课记录对象
     * @return 是否更新成功
     */
    public boolean updateCourseTeacher(CourseTeacher courseTeacher) {
        return courseTeacherMapper.updateCourseTeacher(courseTeacher);
    }

    /**
     * 查询所有开课记录
     * @return 所有开课记录列表
     */
    public List<CourseTeacher> findAll() {
        return courseTeacherMapper.findAll();
    }

    /**
     * 根据学期查询开课记录
     * @param term 学期
     * @return 开课记录列表
     */
    public List<CourseTeacher> findByTerm(String term) {
        return courseTeacherMapper.findByTerm(term);
    }

    /**
     * 生成新的开课编号
     * @return 新的开课编号
     */
    private Integer generateNewCno() {
        // 获取当前最大的cno值并加1
        Integer maxCno = courseTeacherMapper.getMaxCno();
        if (maxCno == null) {
            return 2001; // 起始编号
        }
        return maxCno + 1;
    }
}