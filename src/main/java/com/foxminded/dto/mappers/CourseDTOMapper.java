package com.foxminded.dto.mappers;

import com.foxminded.constants.ErrorMessages;
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

    public Course mapToCourse(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.id());
        course.setName(convertStringToCourseName(courseDTO.name()));
        course.setDescription(courseDTO.description());

        return course;
    }

    private CourseName convertStringToCourseName(String name) {
        try {
            return CourseName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(ErrorMessages.COURSE_WAS_NOT_FOUND, name));
        }
    }

}
