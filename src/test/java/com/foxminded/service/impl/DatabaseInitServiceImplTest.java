package com.foxminded.service.impl;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import com.foxminded.helper.TestDataGenerator;
import com.foxminded.service.CoursesService;
import com.foxminded.service.GroupsService;
import com.foxminded.service.StudentsCoursesService;
import com.foxminded.service.StudentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseInitServiceImplTest {

    @InjectMocks
    private DatabaseInitServiceImpl databaseInitService;
    @Mock
    private StudentsService studentsService;
    @Mock
    private CoursesService coursesService;
    @Mock
    private GroupsService groupsService;
    @Mock
    private StudentsCoursesService studentsCoursesService;
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
        when(groupsService.isEmpty()).thenReturn(true);
        when(coursesService.isEmpty()).thenReturn(true);
        when(studentsService.isEmpty()).thenReturn(true);
        when(studentsCoursesService.isEmpty()).thenReturn(true);

        databaseInitService.init(3, 2, 2);

        verify(testDataGenerator, times(1)).generateTestCourses(3);
        verify(testDataGenerator, times(1)).generateTestGroups(2);
        verify(testDataGenerator, times(1)).generateTestStudents(2);

        verify(coursesService, times(1)).isEmpty();
        verify(groupsService, times(1)).isEmpty();
        verify(studentsService, times(1)).isEmpty();
        verify(studentsCoursesService, times(1)).isEmpty();

        verify(coursesService, times(1)).saveCourses(testCourses);
        verify(groupsService, times(1)).saveGroups(testGroups);
        verify(studentsService, times(1)).saveStudents(testStudents);
        verify(studentsCoursesService, times(1)).saveStudentsCourses(testStudents);

        for (var student : testStudents) {
            assertNotNull(student.getGroup());
            assertFalse(student.getCourses().isEmpty());
        }
    }

}