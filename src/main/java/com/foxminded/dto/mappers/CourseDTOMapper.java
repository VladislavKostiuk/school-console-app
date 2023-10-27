package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.enums.CourseName;

public class CourseDTOMapper {
    public CourseDTO mapToCourseDTO(Course course) {
        return new CourseDTO (
                course.getId(),
                course.getName().toString(),
                course.getDescription()
        );
    }
}
