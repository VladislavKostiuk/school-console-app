package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.service.CoursesService;

import java.util.List;

public class CoursesServiceImpl implements CoursesService {

    private final CourseDao courseDao;

    public CoursesServiceImpl() {
        courseDao = new CourseDao();
    }

    @Override
    public int getIdByName(String name) {
        return courseDao.getIdByName(name);
    }

    @Override
    public List<Course> getCoursesByIds(List<Integer> ids) {
        return courseDao.getCoursesByIds(ids);
    }

    @Override
    public void saveCourses(List<Course> courses) {
        courseDao.saveCourses(courses);
    }

}
