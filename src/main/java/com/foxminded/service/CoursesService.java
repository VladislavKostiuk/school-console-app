package com.foxminded.service;

import com.foxminded.domain.Course;

import java.util.List;

public interface CoursesService {

    Course getCourseByName(String name);

    List<Course> getCoursesByIds(List<Integer> ids);

}
