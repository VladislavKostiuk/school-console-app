package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.mappers.CourseDTOMapper;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoursesServiceImplTest {

    @InjectMocks
    private CoursesServiceImpl coursesService;
    @Mock
    private CourseDao courseDao;
    private CourseDTOMapper courseMapper;

    @BeforeEach
    void init() {
        courseMapper = new CourseDTOMapper();
    }

    @Test
    void testGetCourseByName_Success() {
        String courseName = CourseName.ART.toString();
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(CourseName.ART);

        doReturn(course1).when(courseDao).getCourseByName(courseName);
        CourseDTO actualCourse = courseMapper.mapToCourseDTO(course1);
        assertEquals(actualCourse, coursesService.getCourseByName(courseName));
        verify(courseDao, times(1)).getCourseByName(courseName);
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

}