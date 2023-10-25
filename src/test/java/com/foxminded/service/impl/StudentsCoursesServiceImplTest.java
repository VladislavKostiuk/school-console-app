package com.foxminded.service.impl;

import com.foxminded.dao.StudentsCourseDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsCoursesServiceImplTest {

    @InjectMocks
    StudentsCoursesServiceImpl studentsCoursesService;
    @Mock
    StudentsCourseDao studentsCourseDao;

    @Test
    void testGetStudentsIdByCourseId_Success() {
        List<Integer> studentIds = List.of(1, 2, 3);
        when(studentsCourseDao.getStudentIdsByCourseId(1)).thenReturn(studentIds);
        assertEquals(studentIds, studentsCoursesService.getStudentsIdByCourseId(1));
        verify(studentsCourseDao, times(1)).getStudentIdsByCourseId(1);
    }

    @Test
    void testDeleteStudentCoursesByStudentId_Success() {
        when(studentsCourseDao.deleteStudentCoursesByStudentId(1)).thenReturn(true);
        boolean result = studentsCoursesService.deleteStudentCoursesByStudentId(1);
        assertTrue(result);
        verify(studentsCourseDao).deleteStudentCoursesByStudentId(1);
    }

    @Test
    void testGetCourseIdsByStudentId_Success() {
        List<Integer> courseIds = List.of(1, 2, 3);
        when(studentsCourseDao.getCourseIdsByStudentId(1)).thenReturn(courseIds);
        assertEquals(courseIds, studentsCoursesService.getCourseIdsByStudentId(1));
        verify(studentsCourseDao, times(1)).getCourseIdsByStudentId(1);
    }

    @Test
    void addStudentToCourse_Success() {
        studentsCoursesService.addStudentToCourse(1, 1);
        verify(studentsCourseDao, times(1)).addStudentToCourse(1, 1);
    }

    @Test
    void deleteStudentFromCourse_Success() {
        when(studentsCourseDao.deleteStudentFromCourse(1, 1)).thenReturn(true);
        boolean result = studentsCoursesService.deleteStudentFromCourse(1, 1);
        assertTrue(result);
        verify(studentsCourseDao, times(1)).deleteStudentFromCourse(1, 1);
    }

}