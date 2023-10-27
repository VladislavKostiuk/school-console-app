package com.foxminded.service.impl;

import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.StudentCourseDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.StudentCourseDTOMapper;
import com.foxminded.dto.mappers.StudentDTOMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsServiceImplTest {

    @InjectMocks
    private StudentsServiceImpl studentsService;
    @Mock
    private StudentDao studentDao;
    private StudentDTOMapper studentMapper;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        studentMapper = new StudentDTOMapper();
        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setFirstName("firstName");
        testStudent.setLastName("firstName");
    }

    @Test
    void testGetGroupIdsByStudentNumber_Success() {
        List<Integer> groupIds = List.of(1, 2, 3);
        when(studentDao.getGroupIdsByStudentNumber(5)).thenReturn(groupIds);
        List<Integer> actualGroupIds = studentsService.getGroupIdsByStudentNumber(5);
        assertEquals(groupIds, actualGroupIds);
        verify(studentDao, times(1)).getGroupIdsByStudentNumber(5);
    }

    @Test
    void testGetStudentsByIds_Success() {
        Student testStudent2 = new Student();
        testStudent2.setId(2);

        Map<Student, Integer> studentsMap = new HashMap<>();
        studentsMap.put(testStudent, 1);
        studentsMap.put(testStudent2, 2);

        List<Integer> idList = List.of(1, 2);

        when(studentDao.getStudentsByIds(idList)).thenReturn(studentsMap);
        List<StudentDTO> expectedStudentList = studentMapper.mapToStudentDTOList(studentsMap);
        assertEquals(expectedStudentList, studentsService.getStudentsByIds(idList));
        verify(studentDao, times(1)).getStudentsByIds(idList);
    }

    @Test
    public void testSaveStudent_Success() {
        Group group = new Group();
        group.setId(1);
        testStudent.setGroup(group);

        studentsService.saveStudent(testStudent.getFirstName(), testStudent.getLastName(), testStudent.getGroup().getId());
        verify(studentDao, times(1)).saveStudent(testStudent.getFirstName(), testStudent.getLastName(), testStudent.getGroup().getId());
    }

    @Test
    public void testDeleteStudentById_Success() {
        when(studentDao.deleteStudentById(1)).thenReturn(true);
        boolean result = studentsService.deleteStudentById(1);
        assertTrue(result);
        verify(studentDao, times(1)).deleteStudentById(1);
    }

    @Test
    public void testGetStudentById_Success() {
        Map<Student, Integer> studentMap = new HashMap<>();
        studentMap.put(testStudent, 1);
        when(studentDao.getStudentById(1)).thenReturn(studentMap);
        StudentDTO expectedStudent = studentMapper.mapToStudentDTOList(studentMap).get(0);
        assertEquals(expectedStudent, studentsService.getStudentById(1));
    }

}