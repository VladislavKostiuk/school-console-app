package com.foxminded.dto.mappers;

import com.foxminded.domain.Course;
import com.foxminded.dto.CourseDTO;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseDTOMapperTest {
    private CourseDTOMapper courseMapper;

    @BeforeEach
    void init() {
        courseMapper = new CourseDTOMapper();
    }

    @Test
    void testMapToCourseDTO_Success() {
        int id = 1;
        CourseName name = CourseName.ART;
        String description = "desc";

        Course course = new Course();
        course.setId(id);
        course.setName(name);
        course.setDescription(description);

        CourseDTO expectedCourseDTO = new CourseDTO(
                id,
                name.toString(),
                description
        );

        assertEquals(expectedCourseDTO, courseMapper.mapToCourseDTO(course));
    }

}