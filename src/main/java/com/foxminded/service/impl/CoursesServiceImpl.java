package com.foxminded.service.impl;

import com.foxminded.constants.ErrorMessages;
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
    public int getIdByName(String name) {
        return courseDao.getIdByName(name).orElseThrow(() ->
                new IllegalArgumentException(ErrorMessages.NO_COURSE_WITH_SUCH_NAME));
    }

    @Override
    public List<Course> getCoursesByIds(List<Integer> ids) {
        return courseDao.getCoursesByIds(ids);
    }

    @Override
    public void saveCourses(List<Course> courses) {
        courseDao.saveCourses(courses);
    }

    @Override
    public boolean isEmpty() {
        return courseDao.getCoursesAmount() == 0;
    }


}
