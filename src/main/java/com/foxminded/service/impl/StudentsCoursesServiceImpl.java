package com.foxminded.service.impl;

import com.foxminded.dao.StudentsCourseDao;
import com.foxminded.service.StudentsCoursesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentsCoursesServiceImpl implements StudentsCoursesService {

    private final StudentsCourseDao studentsCourseDao;
    private final Logger logger;

    @Autowired
    public StudentsCoursesServiceImpl(StudentsCourseDao studentsCourseDao) {
        this.studentsCourseDao = studentsCourseDao;
        logger = LoggerFactory.getLogger(StudentsCoursesServiceImpl.class);
    }

    @Override
    public List<Integer> getStudentsIdByCourseId(int courseId) {
        logger.info("Getting student ids by course id {} from {}", courseId, StudentsCourseDao.class);
        return studentsCourseDao.getStudentIdsByCourseId(courseId);
    }

    @Override
    public boolean deleteStudentCoursesByStudentId(int studentId) {
        logger.info("Deleting student courses by student id {} using {}", studentId, StudentsCourseDao.class);
        return studentsCourseDao.deleteStudentCoursesByStudentId(studentId);
    }

    @Override
    public List<Integer> getCourseIdsByStudentId(int studentId) {
        logger.info("Getting course ids by student id {} from {}", studentId, StudentsCourseDao.class);
        return studentsCourseDao.getCourseIdsByStudentId(studentId);
    }

    @Override
    public void addStudentToCourse(int studentId, int courseId) {
        logger.info("Saving student id {} and course id {} using {}", studentId, courseId, StudentsCourseDao.class);
        studentsCourseDao.addStudentToCourse(studentId, courseId);
    }

    @Override
    public boolean deleteStudentFromCourse(int studentId, int courseId) {
        logger.info("Deleting student course with student id {} and course id {} using {}", studentId, courseId, StudentsCourseDao.class);
        return studentsCourseDao.deleteStudentFromCourse(studentId, courseId);
    }

}
