package com.foxminded.dao;

import com.foxminded.AbstractPostgreSQLTestContainer;
import net.bytebuddy.asm.MemberSubstitution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentsCourseDaoTest extends AbstractPostgreSQLTestContainer {

    private StudentsCourseDao studentsCourseDao;

    @BeforeEach
    void init() {
        studentsCourseDao = new StudentsCourseDao(testDatasource);
    }

    @Test
    void testSaveStudentsCourses_Success() {
        List<int[]> studentsCourses = List.of(new int[] {1, 1}, new int[] {1, 3});
        studentsCourseDao.saveStudentsCourses(studentsCourses);

        List<Integer> courseIds = studentsCourseDao.getCourseIdsByStudentId(1);
        studentsCourseDao.deleteStudentFromCourse(1, 1);
        studentsCourseDao.deleteStudentFromCourse(1, 3);

        assertEquals(new HashSet(List.of(1, 2, 3)), new HashSet<>(courseIds));
    }

    @Test
    void testGetStudentsIdByCourseId_Success() {
        assertEquals(List.of(1, 2), studentsCourseDao.getStudentsIdByCourseId(2));
    }

    @Test
    void testGetStudentsIdByCourseId_NoStudentIdsWithSuchCourseId() {
        assertThrows(IllegalArgumentException.class, () -> studentsCourseDao.getStudentsIdByCourseId(100));
    }

    @Test
    void testDeleteStudentCoursesByStudentId_Success() {
        studentsCourseDao.deleteStudentCoursesByStudentId(1);
        assertThrows(IllegalArgumentException.class, () -> studentsCourseDao.getCourseIdsByStudentId(1));

        studentsCourseDao.addStudentToCourse(1, 1);
    }

    @Test
    void testGetCourseIdsByStudentId_Success() {
        assertEquals(List.of(2, 3), studentsCourseDao.getCourseIdsByStudentId(2));
    }

    @Test
    void testGetCourseIdsByStudentId_NoSuchCourseIdsByStudentId() {
        assertThrows(IllegalArgumentException.class, () -> studentsCourseDao.getCourseIdsByStudentId(100));
    }

    @Test
    void testAddStudentCoursesAmount_Success() {
        List<Integer> beforeAdd = studentsCourseDao.getCourseIdsByStudentId(1);
        studentsCourseDao.addStudentToCourse(1, 3);
        List<Integer> afterAdd = studentsCourseDao.getCourseIdsByStudentId(1);
        studentsCourseDao.deleteStudentFromCourse(1, 3);

        assertNotEquals(beforeAdd, afterAdd);
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        studentsCourseDao.addStudentToCourse(2, 1);
        List<Integer> beforeDelete = studentsCourseDao.getCourseIdsByStudentId(2);
        studentsCourseDao.deleteStudentFromCourse(2, 1);
        List<Integer> afterDelete = studentsCourseDao.getCourseIdsByStudentId(2);

        assertNotEquals(beforeDelete, afterDelete);
    }

    @Test
    void getStudentCoursesAmount() {
        assertEquals(3, studentsCourseDao.getStudentCoursesAmount());
    }

}