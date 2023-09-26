package com.foxminded.services.impl;

import com.foxminded.dao.StudentsCoursesDao;
import com.foxminded.domain.Student;
import com.foxminded.services.StudentsCoursesService;

import java.util.List;

public class StudentsCoursesServiceImpl implements StudentsCoursesService {

    private final StudentsCoursesDao studentsCoursesDao;

    public StudentsCoursesServiceImpl() {
        studentsCoursesDao = new StudentsCoursesDao();
    }

    @Override
    public List<Integer> getStudentsIdByCourseId(int courseId) {
        return studentsCoursesDao.getStudentsIdByCourseId(courseId);
    }

    @Override
    public boolean deleteStudentCoursesByStudentId(int studentId) {
        return studentsCoursesDao.deleteStudentCoursesByStudentId(studentId);
    }

    @Override
    public List<Integer> getCourseIdsByStudentId(int studentId) {
        return studentsCoursesDao.getCourseIdsByStudentId(studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        studentsCoursesDao.addStudentToCourse(studentId, courseId);
    }

    @Override
    public boolean deleteStudentFromCourse(int studentId, int courseId) {
        return studentsCoursesDao.deleteStudentFromCourse(studentId, courseId);
    }

    @Override
    public void saveStudentsCourses(List<Student> students) {
        studentsCoursesDao.saveStudentsCourses(students);
    }

}
