package com.foxminded.service;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;

import java.util.List;

public interface CoursesService {

    CourseDTO getCourseByName(String name);

    List<CourseDTO> getCoursesByIds(List<Integer> ids);

}
