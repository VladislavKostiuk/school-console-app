package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoursesServiceImplTest {

    @InjectMocks
    private CoursesServiceImpl coursesService;
    @Mock
    private CourseDao courseDao;

    private CourseMapper courseMapper;

    @Test
    void testGetCourseByName_Success() {
        courseMapper = CourseMapper.INSTANCE;
        CourseName courseName = CourseName.ART;
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(courseName);

        doReturn(course1).when(courseDao).getCourseByName(courseName);
        CourseDTO actualCourse = courseMapper.mapToCourseDTO(course1);
        assertEquals(actualCourse, coursesService.getCourseByName(courseName));
        verify(courseDao, times(1)).getCourseByName(courseName);
    }



}