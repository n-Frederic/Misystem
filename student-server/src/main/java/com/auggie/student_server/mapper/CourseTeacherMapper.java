package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.CourseTeacher;
import com.auggie.student_server.entity.CourseTeacherInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CourseTeacherMapper：针对数据库 studentms.ct 表做 CRUD 操作
 * 统一使用XML配置，移除所有注解
 */
@Repository
@Mapper
public interface CourseTeacherMapper {

    /**
     * 插入开课记录（指定cno）
     */
    boolean insertCourseTeacher(@Param("cno") Integer cno,
                                @Param("cid") Integer cid,
                                @Param("tid") Integer tid,
                                @Param("term") String term);

    /**
     * 插入开课记录（自动生成cno）
     */
    boolean insertCourseTeacherAuto(@Param("cid") Integer cid,
                                    @Param("tid") Integer tid,
                                    @Param("term") String term);

    /**
     * 根据开课编号查询开课记录
     */
    CourseTeacher findByCno(@Param("cno") Integer cno);

    /**
     * 根据条件查询开课记录
     */
    List<CourseTeacher> findBySearch(@Param("cid") Integer cid,
                                     @Param("tid") Integer tid,
                                     @Param("term") String term);

    /**
     * 查询教师在指定学期的课程
     */
    List<Course> findMyCourse(@Param("tid") Integer tid, @Param("term") String term);

    /**
     * 查询教师的开课记录（包含cno信息）
     */
    List<CourseTeacher> findMyCourseTeacher(@Param("tid") Integer tid, @Param("term") String term);

    /**
     * 查询开课信息详情
     */
    List<CourseTeacherInfo> findCourseTeacherInfo(@Param("tid") Integer tid,
                                                  @Param("cno") Integer cno,
                                                  @Param("tname") String tname,
                                                  @Param("tFuzzy") Integer tFuzzy,
                                                  @Param("cid") Integer cid,
                                                  @Param("cname") String cname,
                                                  @Param("cFuzzy") Integer cFuzzy);

    /**
     * 根据开课编号删除开课记录
     */
    boolean deleteByCno(@Param("cno") Integer cno);

    /**
     * 根据CourseTeacher对象删除开课记录
     */
    boolean deleteById(CourseTeacher courseTeacher);

    /**
     * 更新开课记录（通过cno）
     */
    boolean updateCourseTeacherByCno(@Param("cno") Integer cno,
                                     @Param("cid") Integer cid,
                                     @Param("tid") Integer tid,
                                     @Param("term") String term);

    /**
     * 更新开课记录（通过对象）
     */
    boolean updateCourseTeacher(CourseTeacher courseTeacher);

    /**
     * 查询所有开课记录
     */
    List<CourseTeacher> findAll();

    /**
     * 根据学期查询开课记录
     */
    List<CourseTeacher> findByTerm(@Param("term") String term);

    /**
     * 获取最大的开课编号
     */
    Integer getMaxCno();

    /**
     * 检查开课编号是否存在
     */
    Integer checkCnoExists(@Param("cno") Integer cno);

    /**
     * 根据课程ID查询开课记录
     */
    List<CourseTeacher> findByCourseId(@Param("cid") Integer cid);

    /**
     * 根据教师ID查询开课记录
     */
    List<CourseTeacher> findByTeacherId(@Param("tid") Integer tid);

    /**
     * 统计开课记录总数
     */
    Integer countAll();

    /**
     * 统计指定学期的开课记录数
     */
    Integer countByTerm(@Param("term") String term);
}