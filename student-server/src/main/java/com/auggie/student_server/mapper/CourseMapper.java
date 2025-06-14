package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.Course;
import com.auggie.student_server.entity.classroomUseInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface CourseMapper {

    public List<Course> findBySearch(@Param("cid") Integer cid,
                                     @Param("cname") String cname, @Param("fuzzy") Integer fuzzy,
                                     @Param("lowBound") Integer lowBound, @Param("highBound") Integer highBound);


    //    insert
    public boolean insertCourse(@Param("course") Course course);

//    update
    public boolean updateById(@Param("cname") String cname,
                             @Param("ccredit") Integer ccredit);

//    delete
    public boolean deleteById(@Param("cid") Integer cid);

    public List<classroomUseInfo> getAllCourses();
}
