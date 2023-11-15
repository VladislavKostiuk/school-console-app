package com.foxminded.service.impl;

import com.foxminded.mappers.CourseMapperImpl;
import com.foxminded.repository.CourseRepository;
import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {
        CourseMapperImpl.class
})
class CoursesServiceImplTest {

    private CoursesServiceImpl coursesService;
    @Mock
    private CourseRepository courseRepository;

    @Autowired
    private CourseMapper courseMapper;

    @BeforeEach
    void init() {
        coursesService = new CoursesServiceImpl(courseRepository, courseMapper);
    }

    @Test
    void testGetCourseByName_Success() {
        CourseName courseName = CourseName.ART;
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(courseName);

        doReturn(Optional.of(course1)).when(courseRepository).findByName(courseName);
        CourseDTO actualCourse = courseMapper.mapToCourseDTO(course1);
        assertEquals(actualCourse, coursesService.getCourseByName(courseName));
        verify(courseRepository, times(1)).findByName(courseName);
    }

    @Test
    void testGetCourseByName_CourseWithThatNameDoesNotExist() {
        CourseName courseName = CourseName.ART;
        doReturn(Optional.empty()).when(courseRepository).findByName(courseName);
        assertThrows(IllegalArgumentException.class, () -> coursesService.getCourseByName(courseName));
    }



}