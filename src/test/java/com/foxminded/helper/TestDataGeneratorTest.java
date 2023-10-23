package com.foxminded.helper;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestDataGeneratorTest {

    private TestDataGenerator testDataGenerator;

    @BeforeEach
    void setUp() {
        testDataGenerator = new TestDataGenerator();
    }

    @Test
    void testGenerateTestGroups_Success() {
        List<Group> groupList = testDataGenerator.generateTestGroups(3);
        assertEquals(3, groupList.size());

        for (int i = 0; i < 3; i++) {
            Group group = groupList.get(i);
            assertNotNull(group);
            assertEquals(i + 1, group.getId());
            assertTrue(group.getName().matches("^[a-z]{2}-[0-9]{2}$"));
        }
    }

    @Test
    void testGenerateTestStudents_Success() {
        List<Student> studentList = testDataGenerator.generateTestStudents(3);
        assertEquals(3, studentList.size());

        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            assertNotNull(student);
            assertEquals(i + 1, student.getId());
            assertTrue(student.getFirstName().matches("[a-zA-Z]+"));
            assertTrue(student.getLastName().matches("[a-zA-Z]+"));
        }
    }

    @Test
    void testGenerateTestStudents_NoAvailableStudentNames() {
        assertThrows(IllegalArgumentException.class, () -> testDataGenerator.generateTestStudents(Integer.MAX_VALUE));
    }

    @Test
    void testGenerateTestCourses_Success() {
        List<Course> courseList = testDataGenerator.generateTestCourses(3);;
        assertEquals(3, courseList.size());

        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            assertNotNull(course);
            assertEquals(i + 1, course.getId());
            CourseName.valueOf(course.getName().toString());
        }
    }

    @Test
    void testGenerateTestCourses_NoAvailableCoursesNames() {
        assertThrows(IllegalArgumentException.class, () -> testDataGenerator.generateTestCourses(Integer.MAX_VALUE));
    }

}