package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.enums.CourseName;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoursesServiceImplTest {

    @InjectMocks
    CoursesServiceImpl coursesService;
    @Mock
    CourseDao courseDao;

    @Test
    void testGetIdByName_Success() {
        doReturn(Optional.of(1)).when(courseDao).getIdByName("someName");
        assertEquals(1, coursesService.getIdByName("someName"));
        verify(courseDao, times(1)).getIdByName("someName");
    }

    @Test
    void testGetIdByName_NoCourseWithSuchName() {
        doReturn(Optional.empty()).when(courseDao).getIdByName("nonExistingName");
        assertThrows(IllegalArgumentException.class, () -> coursesService.getIdByName("nonExistingName"));
        verify(courseDao, times(1)).getIdByName("nonExistingName");
    }

    @Test
    void testGetCoursesByIds_Success() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(CourseName.ART);

        Course course2 = new Course();
        course2.setId(2);
        course2.setName(CourseName.MATH);

        List<Course> expectedList = List.of(course1, course2);
        doReturn(expectedList).when(courseDao).getCoursesByIds(List.of(1, 2));
        List<Course> actualList = courseDao.getCoursesByIds(List.of(1, 2));

        assertEquals(expectedList, actualList);
        verify(courseDao, times(1)).getCoursesByIds(List.of(1, 2));
    }

    @Test
    void testSaveCourses_Success() {
       List<Course> courseList = List.of(new Course());
       coursesService.saveCourses(courseList);
       verify(courseDao, times(1)).saveCourses(courseList);
    }

    @Test
    void testIsEmpty_Success() {
        doReturn(0).when(courseDao).getCoursesAmount();
        assertTrue(coursesService.isEmpty());
        verify(courseDao, times(1)).getCoursesAmount();

        doReturn(3).when(courseDao).getCoursesAmount();
        assertFalse(coursesService.isEmpty());
        verify(courseDao, times(2)).getCoursesAmount();
    }

}