package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.mappers.GroupMapper;
import com.foxminded.mappers.StudentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class StudentMapperTest {
    private StudentMapper studentMapper;
    private GroupMapper groupMapper;
    private CourseMapper courseMapper;
    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void init() {
        studentMapper = StudentMapper.INSTANCE;
        groupMapper = GroupMapper.INSTANCE;
        courseMapper = CourseMapper.INSTANCE;
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
                groupMapper.mapToGroupDTO(group),
                firstName,
                lastName,
                testStudent.getCourses()
                        .stream()
                        .map(courseMapper::mapToCourseDTO)
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