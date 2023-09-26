package com.foxminded.services;

import com.foxminded.domain.Course;

import java.util.List;

public interface CoursesService {

    int getIdByName(String name);

    List<Course> getCoursesByIds(List<Integer> ids);

    void saveCourses(List<Course> courses);

}
