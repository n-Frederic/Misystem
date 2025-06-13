package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;



import com.auggie.student_server.entity.ScInfo;
import com.auggie.student_server.entity.StudentCourse;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * StudentCourseMapper：针对数据库 studentms.sc 表做 CRUD 操作
 */
@Mapper
@Repository
public interface StudentCourseMapper {

    public  List<CTinfo> findByStudentId(@Param("sid") Integer sid, @Param("term") String term);

    public boolean findsc(@Param("sid")Integer sid,@Param("cno")Integer cno);
    @Select("SELECT DISTINCT sc.term FROM studentms.sc sc")
    public  List<String> findAllTerm();

    @Select("SELECT * FROM studentms.sc WHERE sid = #{sc.sid} AND cno = #{sc.cno}  AND term = #{sc.term}")
    public List<StudentCourse>findBySC(@Param("sc")StudentCourse studentCourse);
//???

    @Insert("INSERT INTO studentms.sc (sid, cno, term) VALUES (#{sc.sid}, #{sc.cno}, #{sc.term})")
    public boolean insert(@Param("sc")StudentCourse studentCourse);

    @Delete("DELETE FROM studentms.sc WHERE sid = #{sc.sid} AND cno = #{sc.cno}")
    public boolean deleteBySC(@Param("sc") StudentCourse sc);


    public List<ScInfo> findBySearch(@Param("sid") Integer sid,
                                      @Param("sname") String sname,
                                      @Param("sFuzzy") Integer sFuzzy,
                                      @Param("cid") Integer cid,
                                      @Param("cname") String cname,
                                      @Param("cFuzzy") Integer cFuzzy,
                                      @Param("tid") Integer tid,
                                      @Param("tname") String tname,
                                      @Param("tFuzzy") Integer tFuzzy,
                                      @Param("lowBound") Integer lowBound,
                                      @Param("highBound") Integer highBound,
                                      @Param("term") String term);


    @Update("UPDATE studentms.sc SET sc.daily_grade = #{grade} WHERE sc.sid = #{sid} AND sc.cno = #{cno} AND sc.term = #{term}")
    public boolean updateById(@Param("sid") Integer sid,
                              @Param("cno") Integer cno,
                              @Param("term") String term,
                              @Param("grade") Integer grade);

//到这里
}