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