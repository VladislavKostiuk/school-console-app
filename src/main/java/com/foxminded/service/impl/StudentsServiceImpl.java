package com.foxminded.service.impl;

import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Student;
import com.foxminded.service.StudentsService;

import java.util.List;
import java.util.Map;

public class StudentsServiceImpl implements StudentsService {

    private final StudentDao studentDao;

    public StudentsServiceImpl() {
        studentDao = new StudentDao();
    }

    @Override
    public List<Integer> getGroupsIdByStudentNumber(int number) {
        return studentDao.getGroupsIdByStudentNumber(number);
    }

    @Override
    public Map<Student, Integer> getStudentsByIds(List<Integer> ids) {
        return studentDao.getStudentsByIds(ids);
    }

    @Override
    public void saveStudent(String firstName, String lastName, int groupId) {
        studentDao.saveStudent(firstName, lastName, groupId);
    }

    @Override
    public boolean deleteStudentById(int id) {
        return studentDao.deleteStudentById(id);
    }

    @Override
    public Map<Student, Integer> getStudentById(int id) {
        return studentDao.getStudentById(id);
    }

    @Override
    public void saveStudents(List<Student> students) {
        studentDao.saveStudents(students);
    }

}
