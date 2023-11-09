package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentDTOMapperTest {
    private StudentDTOMapper studentMapper;
    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void init() {
        studentMapper = new StudentDTOMapper();

        Group group = new Group();
        group.setId(1);
        group.setName("qw-12");

        int id = 1;
        String firstName = "first name";
        String lastName = "last name";

        testStudent = new Student();
        testStudent.setId(id);
        testStudent.setGroup(group);
        testStudent.setFirstName(firstName);
        testStudent.setLastName(lastName);

        testStudentDTO = new StudentDTO(
                id,
                group.getId(),
                group.getName(),
                firstName,
                lastName,
                testStudent.getCourses()
                        .stream()
                        .map(Course::getName)
                        .map(CourseName::toString)
                        .toList()
        );
    }

    @Test
    void testMapToStudentDTOList_Success() {
        assertEquals(testStudentDTO, studentMapper.mapToStudentDTO(testStudent));
    }

    @Test
    void testMapToStudent() {
        assertEquals(testStudent, studentMapper.mapToStudent(testStudentDTO));
    }

}