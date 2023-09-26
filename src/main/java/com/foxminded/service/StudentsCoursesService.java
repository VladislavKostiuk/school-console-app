package com.foxminded.service;

import com.foxminded.domain.Student;

import java.util.List;

public interface StudentsCoursesService {

    List<Integer> getStudentsIdByCourseId(int courseId);

    boolean deleteStudentCoursesByStudentId(int studentId);

    List<Integer> getCourseIdsByStudentId(int studentId);

    void addStudentToCourse(int studentId, int courseId);

    boolean deleteStudentFromCourse(int studentId, int courseId);

    void saveStudentsCourses(List<Student> students);

}
