package com.foxminded.service;

import com.foxminded.domain.Student;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;

import java.util.List;

public interface StudentsService {

    List<GroupDTO> getGroupsByStudentAmount(int number);

    void saveStudent(String firstName, String lastName, GroupDTO groupDTO);

    void deleteStudentById(int id);

    StudentDTO getStudentById(int id);

    List<StudentDTO> getStudentsByCourse(CourseDTO courseDTO);

    void addStudentToCourse(StudentDTO studentDTO, CourseDTO courseDTO);

    void deleteStudentFromCourse(StudentDTO studentDTO, CourseDTO courseDTO);

}
