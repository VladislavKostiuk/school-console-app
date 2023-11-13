package com.foxminded.service;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.enums.CourseName;

import java.util.List;

public interface CoursesService {

    CourseDTO getCourseByName(CourseName courseName);

}
