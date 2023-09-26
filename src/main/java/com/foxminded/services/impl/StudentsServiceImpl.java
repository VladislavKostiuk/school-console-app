package com.foxminded.services.impl;

import com.foxminded.dao.StudentsDao;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.services.StudentsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsServiceImpl implements StudentsService {

    private final StudentsDao studentsDao;

    public StudentsServiceImpl() {
        studentsDao = new StudentsDao();
    }

    @Override
    public List<Integer> getGroupsIdByStudentNumber(int number) {
        return studentsDao.getGroupsIdByStudentNumber(number);
    }

    @Override
    public Map<Student, Integer> getStudentsByIds(List<Integer> ids) {
        return studentsDao.getStudentsByIds(ids);
    }

    @Override
    public void saveStudent(String firstName, String lastName, int groupId) {
        studentsDao.saveStudent(firstName, lastName, groupId);
    }

    @Override
    public boolean deleteStudentById(int id) {
        return studentsDao.deleteStudentById(id);
    }

    @Override
    public Map<Student, Integer> getStudentById(int id) {
        return studentsDao.getStudentById(id);
    }

    @Override
    public void saveStudents(List<Student> students) {
        studentsDao.saveStudents(students);
    }

}
