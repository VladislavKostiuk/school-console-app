package com.foxminded.dto.mappers;

import com.foxminded.dto.StudentCourseDTO;
import com.foxminded.dto.StudentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentCourseDTOMapperTest {

    private StudentCourseDTOMapper studentCourseMapper;

    @BeforeEach
    void init() {
        studentCourseMapper = new StudentCourseDTOMapper();
    }

    @Test
    void testMapToStudentCourseDTO() {
        int id = 1;
        int groupId = 1;
        String firstName = "first name";
        String lastName = "last name";

        List<String> courseNames = List.of("course1", "course2");
        StudentDTO studentDTO = new StudentDTO(
                id,
                groupId,
                firstName,
                lastName
        );

        StudentCourseDTO studentCourseDTO = new StudentCourseDTO(
                id,
                groupId,
                firstName,
                lastName,
                courseNames
        );

        assertEquals(studentCourseDTO, studentCourseMapper.mapToStudentCourseDTO(studentDTO, courseNames));
    }

}