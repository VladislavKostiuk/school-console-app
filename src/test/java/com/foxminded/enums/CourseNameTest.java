package com.foxminded.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class CourseNameTest {

    @Test
    void testConvertStringToCourseName_Success() {
        assertEquals(CourseName.ART, CourseName.convertStringToCourseName("ART"));
    }

    @Test
    void testConvertStringToCourseName_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> CourseName.convertStringToCourseName("non-existent name"));
    }

}