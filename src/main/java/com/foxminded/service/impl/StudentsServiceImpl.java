package com.foxminded.service.impl;

import com.foxminded.dao.StudentDao;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.StudentDTOMapper;
import com.foxminded.service.StudentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentsServiceImpl implements StudentsService {

    private final StudentDao studentDao;
    private final StudentDTOMapper studentMapper;
    private final Logger logger;

    @Autowired
    public StudentsServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
        studentMapper = new StudentDTOMapper();
        logger = LoggerFactory.getLogger(StudentsServiceImpl.class);
    }

    @Override
    public List<Integer> getGroupIdsByStudentNumber(int number) {
        logger.info("Getting group ids by amount of students {} from {}", number, StudentDao.class);
        return studentDao.getGroupIdsByStudentNumber(number);
    }

    @Override
    public List<StudentDTO> getStudentsByIds(List<Integer> ids) {
        logger.info("Getting students by ids: {} from {}", ids, StudentDao.class);
        return studentMapper.mapToStudentDTOList(studentDao.getStudentsByIds(ids));
    }

    @Override
    public void saveStudent(String firstName, String lastName, int groupId) {
        logger.info("Saving student with first name {} and last name {} using {}", firstName, lastName, StudentDao.class);
        studentDao.saveStudent(firstName, lastName, groupId);
    }

    @Override
    public boolean deleteStudentById(int id) {
        logger.info("Deleting student by id {} using {}", id, StudentDao.class);
        return studentDao.deleteStudentById(id);
    }

    @Override
    public StudentDTO getStudentById(int id) {
        logger.info("Getting student by id {} from {}", id, StudentDao.class);
        return studentMapper.mapToStudentDTOList(studentDao.getStudentById(id)).get(0);
    }

}
