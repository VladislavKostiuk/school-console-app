package com.foxminded.dto.mappers;

import com.foxminded.dto.StudentCourseDTO;
import com.foxminded.dto.StudentDTO;

import java.util.List;

public class StudentCourseDTOMapper {

    public StudentCourseDTO mapToStudentCourseDTO(StudentDTO studentDTO, List<String> courses) {
        return new StudentCourseDTO(
                studentDTO.id(),
                studentDTO.groupId(),
                studentDTO.firstName(),
                studentDTO.lastName(),
                courses
        );
    }

}
