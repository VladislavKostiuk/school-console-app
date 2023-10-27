package com.foxminded.service.impl;

import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.StudentDTOMapper;
import com.foxminded.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentsServiceImpl implements StudentsService {

    private final StudentDao studentDao;
    private final StudentDTOMapper studentMapper;

    @Autowired
    public StudentsServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
        studentMapper = new StudentDTOMapper();
    }

    @Override
    public List<Integer> getGroupIdsByStudentNumber(int number) {
        return studentDao.getGroupIdsByStudentNumber(number);
    }

    @Override
    public List<StudentDTO> getStudentsByIds(List<Integer> ids) {
        return studentMapper.mapToStudentDTOList(studentDao.getStudentsByIds(ids));
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
    public StudentDTO getStudentById(int id) {
        return studentMapper.mapToStudentDTOList(studentDao.getStudentById(id)).get(0);
    }

}
