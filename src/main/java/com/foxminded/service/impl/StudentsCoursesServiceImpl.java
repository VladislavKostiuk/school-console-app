package com.foxminded.service.impl;

import com.foxminded.dao.StudentsCourseDao;
import com.foxminded.domain.Student;
import com.foxminded.service.StudentsCoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentsCoursesServiceImpl implements StudentsCoursesService {

    private final StudentsCourseDao studentsCourseDao;

    @Autowired
    public StudentsCoursesServiceImpl(StudentsCourseDao studentsCourseDao) {
        this.studentsCourseDao = studentsCourseDao;
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
        List<int[]> studentsCourses = new ArrayList<>();
        for (var student : students) {
            for (var course : student.getCourses()) {
                studentsCourses.add(new int[] {student.getId(), course.getId()});
            }
        }
        studentsCourseDao.saveStudentsCourses(studentsCourses);
    }

    @Override
    public boolean isEmpty() {
        return studentsCourseDao.getStudentCoursesAmount() == 0;
    }

}
