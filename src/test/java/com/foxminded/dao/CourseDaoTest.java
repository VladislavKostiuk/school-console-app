package com.foxminded.dao;

import com.foxminded.AbstractPostgreSQLTestContainer;
import com.foxminded.domain.Course;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseDaoTest extends AbstractPostgreSQLTestContainer {

    private CourseDao courseDao;

    @BeforeEach
    void init() {
        courseDao = new CourseDao(testDatasource);
    }

    @Test
    void testGetCoursesAmount() {
        int actualAmount = courseDao.getCoursesAmount();
        assertEquals(3, actualAmount);
    }

    @Test
    void testSaveCourses_Success() {
        Course courseA = new Course();
        courseA.setName(CourseName.BIOLOGY);
        courseA.setDescription("desc");

        Course courseB = new Course();
        courseB.setName(CourseName.FINANCE);
        courseB.setDescription("desc");

        courseDao.saveCourses(List.of(courseA, courseB));

        Course fourth = courseDao.getCourseById(4);
        Course fifth = courseDao.getCourseById(5);

        assertEquals(4, fourth.getId());
        assertEquals(CourseName.BIOLOGY, fourth.getName());
        assertEquals(5, fifth.getId());
        assertEquals("desc", fifth.getDescription());
    }

    @Test
    void testGetCourseById_Success() {
        Course actual = courseDao.getCourseById(1);

        Course expected = new Course();
        expected.setId(1);
        expected.setName(CourseName.ART);
        expected.setDescription("desc1");
        assertEquals(expected, actual);
    }

    @Test
    void testGetCourseById_NoCourseWithSuchId() {
        assertThrows(IllegalArgumentException.class, () -> courseDao.getCourseById(100));
    }

    @Test
    void testGetIdByName_Success() {
        int id = courseDao.getIdByName("ART").get();
        assertEquals(1, id);
    }

    @Test
    void testGetCoursesByIds() {
        Course firstExpected = new Course();
        firstExpected.setId(1);
        firstExpected.setName(CourseName.ART);
        firstExpected.setDescription("desc1");

        Course secondExpected = new Course();
        secondExpected.setId(2);
        secondExpected.setName(CourseName.MATH);
        secondExpected.setDescription("desc2");

        List<Course> actualCourses = courseDao.getCoursesByIds(List.of(1, 2));
        List<Course> expectedCourses = List.of(firstExpected, secondExpected);

        assertEquals(expectedCourses, actualCourses);
    }

    @Test
    void testGetCoursesByIds_NoCoursesWithSuchIds() {
        assertThrows(IllegalArgumentException.class, () -> courseDao.getCoursesByIds(List.of(1, 200, 300)));
    }

}