package com.foxminded.service.impl;

import com.foxminded.dao.StudentsCourseDao;
import com.foxminded.domain.Student;
import com.foxminded.service.StudentsCoursesService;

import java.util.List;

public class StudentsCoursesServiceImpl implements StudentsCoursesService {

    private final StudentsCourseDao studentsCourseDao;

    public StudentsCoursesServiceImpl() {
        studentsCourseDao = new StudentsCourseDao();
    }

    @Override
    public List<Integer> getStudentsIdByCourseId(int courseId) {
        return studentsCourseDao.getStudentsIdByCourseId(courseId);
    }

    @Override
    public boolean deleteStudentCoursesByStudentId(int studentId) {
        return studentsCourseDao.deleteStudentCoursesByStudentId(studentId);
    }

    @Override
    public List<Integer> getCourseIdsByStudentId(int studentId) {
        return studentsCourseDao.getCourseIdsByStudentId(studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        studentsCourseDao.addStudentToCourse(studentId, courseId);
    }

    @Override
    public boolean deleteStudentFromCourse(int studentId, int courseId) {
        return studentsCourseDao.deleteStudentFromCourse(studentId, courseId);
    }

    @Override
    public void saveStudentsCourses(List<Student> students) {
        studentsCourseDao.saveStudentsCourses(students);
    }

}
