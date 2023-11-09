package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseDTOMapperTest {
    private CourseDTOMapper courseMapper;
    private Course testCourse;
    private CourseDTO testCourseDTO;

    @BeforeEach
    void init() {
        courseMapper = new CourseDTOMapper();

        int id = 1;
        CourseName name = CourseName.ART;
        String description = "desc";

        testCourse = new Course();
        testCourse.setId(id);
        testCourse.setName(name);
        testCourse.setDescription(description);

        testCourseDTO = new CourseDTO(
                id,
                name.toString(),
                description
        );
    }

    @Test
    void testMapToCourseDTO_Success() {
        assertEquals(testCourseDTO, courseMapper.mapToCourseDTO(testCourse));
    }

    @Test
    void testMapToCourse_Success() {
        assertEquals(testCourse, courseMapper.mapToCourse(testCourseDTO));
    }

    @Test
    void testMapToCourse_CourseWithThatNameDoesNotExist() {
        testCourseDTO = new CourseDTO(
                1,
                "non-existent course name",
                "desc"
        );
        assertThrows(IllegalArgumentException.class, () -> courseMapper.mapToCourse(testCourseDTO));
    }

}