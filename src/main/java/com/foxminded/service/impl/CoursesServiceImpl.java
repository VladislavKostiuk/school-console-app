package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CourseDao courseDao;

    @Autowired
    public CoursesServiceImpl(CourseDao courseDao) {
        this.courseDao = courseDao;
    }

    @Override
    public Course getCourseByName(String name) {
        return courseDao.getCourseByName(name);
    }

    @Override
    public List<Course> getCoursesByIds(List<Integer> ids) {
        return courseDao.getCoursesByIds(ids);
    }

}
