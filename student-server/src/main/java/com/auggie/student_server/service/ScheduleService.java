package com.auggie.student_server.service;

import com.auggie.student_server.entity.CourseDTO;
import com.auggie.student_server.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    public List<CourseDTO> findCoursesByStudentAndTerm(String sid, String term) {
        List<CourseDTO> list = scheduleMapper.getStudentSchedule(sid,term);
        return list;
    }
}
