package com.foxminded.dao;

import com.foxminded.AbstractDaoTest;
import com.foxminded.domain.Course;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest extends AbstractDaoTest {

    private CourseDao courseDao;

    @BeforeEach
    void init() {
        courseDao = new CourseDao(entityManager);
    }

    @Test
    void testGetCourseByName_Success() {
        CourseName courseName = CourseName.ART;

        Course expectedCourse = new Course();
        expectedCourse.setId(1);
        expectedCourse.setName(courseName);
        expectedCourse.setDescription("desc1");

        assertEquals(expectedCourse, courseDao.getCourseByName(courseName));
    }

    @Test
    void testGetCoursesAmount_Success() {
        assertEquals(3, courseDao.getCoursesAmount());
    }

    @Test
    void testSaveCourses_Success() {
        Course testCourse1 = new Course();
        testCourse1.setName(CourseName.MEDICINE);
        testCourse1.setDescription("desc");

        Course testCourse2 = new Course();
        testCourse2.setName(CourseName.FINANCE);
        testCourse2.setDescription("desc");

        List<Course> testCourses = List.of(testCourse1, testCourse2);
        courseDao.saveCourses(testCourses);
        assertEquals(5, courseDao.getCoursesAmount());
    }

}