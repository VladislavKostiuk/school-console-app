package com.foxminded.dao;

import com.foxminded.AbstractPostgreSQLTestContainer;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class StudentDaoTest extends AbstractPostgreSQLTestContainer {

    private StudentDao studentDao;
    private Group testGroup;
    private Course course2;
    private Student student2;

    @BeforeEach
    void init() {
        studentDao = new StudentDao(entityManager);
        testGroup = new Group();
        testGroup.setId(1);
        testGroup.setName("gr1");

        Group group = new Group();
        group.setId(3);
        group.setName("gr3");

        course2 = new Course();
        course2.setId(2);
        course2.setDescription("desc2");
        course2.setName(CourseName.MATH);

        student2 = new Student();
        student2.setId(2);
        student2.setFirstName("firstName2");
        student2.setLastName("lastName2");
        student2.setGroup(group);
        student2.setCourses(List.of(course2));
    }

    @Test
    void testSaveStudents_Success() {
        Student student1 = new Student();
        student1.setFirstName("first name");
        student1.setLastName("last name");
        student1.setGroup(testGroup);

        Student student2 = new Student();
        student2.setFirstName("first name");
        student2.setLastName("last name");
        student2.setGroup(testGroup);

        studentDao.saveStudents(List.of(student1, student2));
        assertEquals(5, studentDao.getStudentsAmount());
    }

    @Test
    void testGetGroupsByStudentAmount_Success() {
        assertEquals(List.of(testGroup), studentDao.getGroupsByStudentAmount(1));
    }

    @Test
    void testSaveStudent_Success() {
        Student student4 = new Student();
        student4.setFirstName("first name");
        student4.setLastName("last name");
        student4.setGroup(testGroup);

        studentDao.saveStudent(student4);
        assertEquals(4, studentDao.getStudentsAmount());
    }

    @Test
    void testDeleteStudentById_Success() {
        assertTrue( studentDao.deleteStudentById(1));
        assertEquals(2, studentDao.getStudentsAmount());
    }

    @Test
    void testGetStudentById_Success() {
        assertEquals(student2, studentDao.getStudentById(2));
    }

    @Test
    void testGetStudentsAmount_Success() {
        assertEquals(3, studentDao.getStudentsAmount());
    }

    @Test
    void testGetStudentsByCourse_Success() {
        assertEquals(List.of(student2), studentDao.getStudentsByCourse(course2));
    }

    @Test
    void testAddStudentToCourse_Success() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setDescription("desc1");
        course1.setName(CourseName.ART);

        studentDao.addStudentToCourse(student2, course1);
        List<Course> expectedCourses = List.of(course1, course2);
        List<Course> actualCourses = studentDao.getStudentById(2).getCourses();
        assertEquals(new HashSet<>(expectedCourses), new HashSet<>(actualCourses));
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        studentDao.deleteStudentFromCourse(student2, course2);
        List<Course> actualCourses = studentDao.getStudentById(2).getCourses();
        assertEquals(new HashSet<>(), new HashSet<>(actualCourses));
    }

}