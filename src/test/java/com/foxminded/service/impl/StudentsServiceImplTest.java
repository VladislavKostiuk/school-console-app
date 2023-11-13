package com.foxminded.service.impl;

import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.mappers.CourseMapper;
import com.foxminded.mappers.GroupMapper;
import com.foxminded.mappers.StudentMapper;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentsServiceImplTest {

    @InjectMocks
    private StudentsServiceImpl studentsService;
    @Mock
    private StudentDao studentDao;
    private StudentMapper studentMapper;
    private GroupMapper groupMapper;
    private CourseMapper courseMapper;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        studentMapper = StudentMapper.INSTANCE;
        groupMapper = GroupMapper.INSTANCE;
        courseMapper = CourseMapper.INSTANCE;
        Group group = new Group();
        group.setId(1);

        testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName(CourseName.MEDICINE);
        testCourse.setDescription("desc");

        testStudent = new Student();
        testStudent.setId(1);
        testStudent.setFirstName("firstName");
        testStudent.setLastName("lastName");
        testStudent.setGroup(group);


    }

    @Test
    void testGetGroupsByStudentAmount_Success() {
        Group group1 = new Group();
        group1.setId(1);
        group1.setName("name1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("name2");

        List<GroupDTO> expectedGroupDTOs = Stream.of(group1, group2).map(groupMapper::mapToGroupDTO).toList();
        when(studentDao.getGroupsByStudentAmount(anyInt())).thenReturn(List.of(group1, group2));
        List<GroupDTO> actualGroupDTOs = studentsService.getGroupsByStudentAmount(anyInt());
        assertEquals(expectedGroupDTOs, actualGroupDTOs);
        verify(studentDao, times(1)).getGroupsByStudentAmount(anyInt());
    }

    @Test
    public void testSaveStudent_Success() {
        Group group = new Group();
        group.setId(1);
        GroupDTO groupDTO = groupMapper.mapToGroupDTO(group);
        studentsService.saveStudent(testStudent.getFirstName(), testStudent.getLastName(), groupDTO);
        verify(studentDao, times(1)).saveStudent(any());
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
        when(studentDao.getStudentById(1)).thenReturn(testStudent);
        StudentDTO expectedStudent = studentMapper.mapToStudentDTO(testStudent);
        assertEquals(expectedStudent, studentsService.getStudentById(1));
    }

    @Test
    public void testGetStudentByCourse_Success() {
        testStudent.getCourses().add(testCourse);

        when(studentDao.getStudentsByCourse(testCourse)).thenReturn(List.of(testStudent));
        List<StudentDTO> expectedStudentDTOs = Stream.of(testStudent).map(studentMapper::mapToStudentDTO).toList();
        List<StudentDTO> actualStudentDTOs = studentsService.getStudentsByCourse(courseMapper.mapToCourseDTO(testCourse));
        assertEquals(expectedStudentDTOs, actualStudentDTOs);
        verify(studentDao, times(1)).getStudentsByCourse(testCourse);
    }

    @Test
    void testAddStudentToCourse_Success() {
        studentsService.addStudentToCourse(
                studentMapper.mapToStudentDTO(testStudent), courseMapper.mapToCourseDTO(testCourse)
        );
        verify(studentDao, times(1)).addStudentToCourse(testStudent, testCourse);
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        when(studentDao.deleteStudentFromCourse(testStudent, testCourse)).thenReturn(true);
        assertTrue(studentsService.deleteStudentFromCourse(
                studentMapper.mapToStudentDTO(testStudent), courseMapper.mapToCourseDTO(testCourse)
        ));
        verify(studentDao, times(1)).deleteStudentFromCourse(testStudent, testCourse);
    }

}