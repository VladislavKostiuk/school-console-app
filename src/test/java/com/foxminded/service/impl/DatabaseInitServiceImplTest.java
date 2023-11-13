package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.dao.GroupDao;
import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import com.foxminded.helper.TestDataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseInitServiceImplTest {

    @InjectMocks
    private DatabaseInitServiceImpl databaseInitService;
    @Mock
    private StudentDao studentDao;
    @Mock
    private CourseDao courseDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private TestDataGenerator testDataGenerator;
    private List<Course> testCourses;
    private List<Group> testGroups;
    private List<Student> testStudents;

    @BeforeEach
    void setUp() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(CourseName.ART);

        Course course2 = new Course();
        course2.setId(2);
        course2.setName(CourseName.MATH);

        Course course3 = new Course();
        course3.setId(3);
        course3.setName(CourseName.FINANCE);

        testCourses = List.of(course1, course2, course3);

        Group group1 = new Group();
        group1.setId(1);
        group1.setName("group1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("group2");

        testGroups = List.of(group1, group2);

        Student student1 = new Student();
        student1.setId(1);
        student1.setFirstName("firstName1");
        student1.setLastName("lastName1");

        Student student2 = new Student();
        student2.setId(2);
        student2.setFirstName("firstName2");
        student2.setLastName("lastName2");

        testStudents = List.of(student1, student2);
    }

    @Test
    void testInit_Success() {
        when(testDataGenerator.generateTestCourses(3)).thenReturn(testCourses);
        when(testDataGenerator.generateTestGroups(2)).thenReturn(testGroups);
        when(testDataGenerator.generateTestStudents(2)).thenReturn(testStudents);
        when(groupDao.getGroupsAmount()).thenReturn(0);
        when(courseDao.getCoursesAmount()).thenReturn(0);
        when(studentDao.getStudentsAmount()).thenReturn(0);

        databaseInitService.init(3, 2, 2);

        verify(testDataGenerator, times(1)).generateTestCourses(3);
        verify(testDataGenerator, times(1)).generateTestGroups(2);
        verify(testDataGenerator, times(1)).generateTestStudents(2);

        verify(groupDao, times(1)).getGroupsAmount();
        verify(courseDao, times(1)).getCoursesAmount();
        verify(studentDao, times(1)).getStudentsAmount();

        verify(groupDao, times(1)).saveGroups(testGroups);
        verify(courseDao, times(1)).saveCourses(testCourses);
        verify(studentDao, times(1)).saveStudents(testStudents);

        for (var student : testStudents) {
            assertNotNull(student.getGroup());
            assertFalse(student.getCourses().isEmpty());
        }
    }

}