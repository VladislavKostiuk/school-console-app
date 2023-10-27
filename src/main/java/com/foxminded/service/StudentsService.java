package com.foxminded.service;

import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;

import java.util.List;
import java.util.Map;

public interface StudentsService {

    List<Integer> getGroupIdsByStudentNumber(int number);

    List<StudentDTO> getStudentsByIds(List<Integer> ids);

    void saveStudent(String firstName, String lastName, int groupId);

    boolean deleteStudentById(int id);

    StudentDTO getStudentById(int id);

}
