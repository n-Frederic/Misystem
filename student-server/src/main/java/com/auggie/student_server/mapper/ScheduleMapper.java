package com.auggie.student_server.mapper;

import com.auggie.student_server.entity.CourseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    List<CourseDTO> getStudentSchedule(@Param("sid") String sid, @Param("term") String term);
}
