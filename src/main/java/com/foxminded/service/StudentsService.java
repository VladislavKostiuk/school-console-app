package com.foxminded.service;

import com.foxminded.domain.Student;

import java.util.List;
import java.util.Map;

public interface StudentsService {

    List<Integer> getGroupIdsByStudentNumber(int number);

    Map<Student, Integer> getStudentsByIds(List<Integer> ids);

    void saveStudent(String firstName, String lastName, int groupId);

    boolean deleteStudentById(int id);

    Map<Student, Integer> getStudentById(int id);

}
