package com.foxminded.services.impl;

import com.foxminded.dao.CoursesDao;
import com.foxminded.domain.Course;
import com.foxminded.services.CoursesService;

import java.util.List;

public class CoursesServiceImpl implements CoursesService {

    private final CoursesDao coursesDao;

    public CoursesServiceImpl() {
        coursesDao = new CoursesDao();
    }

    @Override
    public int getIdByName(String name) {
        return coursesDao.getIdByName(name);
    }

    @Override
    public List<Course> getCoursesByIds(List<Integer> ids) {
        return coursesDao.getCoursesByIds(ids);
    }

    @Override
    public void saveCourses(List<Course> courses) {
        coursesDao.saveCourses(courses);
    }

}
