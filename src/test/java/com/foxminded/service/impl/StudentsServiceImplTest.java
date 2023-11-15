package com.foxminded.service.impl;

import com.foxminded.mappers.*;
import com.foxminded.repository.StudentRepository;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {
        StudentMapperImpl.class,
        GroupMapperImpl.class,
        CourseMapperImpl.class
})
class StudentsServiceImplTest {


    private StudentsServiceImpl studentsService;
    @Mock
    private StudentRepository studentRepository;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private CourseMapper courseMapper;
    private Student testStudent;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        studentsService = new StudentsServiceImpl(
                studentRepository, studentMapper, groupMapper, courseMapper
        );
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
        when(studentRepository.findGroupsByStudentAmount(anyLong())).thenReturn(List.of(group1, group2));
        List<GroupDTO> actualGroupDTOs = studentsService.getGroupsByStudentAmount(20);
        assertEquals(expectedGroupDTOs, actualGroupDTOs);
        verify(studentRepository, times(1)).findGroupsByStudentAmount(anyLong());
    }

    @Test
    public void testSaveStudent_Success() {
        Group group = new Group();
        group.setId(1);
        GroupDTO groupDTO = groupMapper.mapToGroupDTO(group);
        studentsService.saveStudent(testStudent.getFirstName(), testStudent.getLastName(), groupDTO);
        verify(studentRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteStudentById_Success() {
        studentsService.deleteStudentById(1);
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        StudentDTO expectedStudent = studentMapper.mapToStudentDTO(testStudent);
        assertEquals(expectedStudent, studentsService.getStudentById(1));
    }

    @Test
    public void testGetStudentById_StudentWithThatIdDoesNotExist() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> studentsService.getStudentById(1));
    }

    @Test
    public void testGetStudentsByCourse_Success() {
        testStudent.getCourses().add(testCourse);

        when(studentRepository.findStudentsByCoursesContaining(testCourse)).thenReturn(List.of(testStudent));
        List<StudentDTO> expectedStudentDTOs = Stream.of(testStudent).map(studentMapper::mapToStudentDTO).toList();
        List<StudentDTO> actualStudentDTOs = studentsService.getStudentsByCourse(courseMapper.mapToCourseDTO(testCourse));
        assertEquals(expectedStudentDTOs, actualStudentDTOs);
        verify(studentRepository, times(1)).findStudentsByCoursesContaining(testCourse);
    }

    @Test
    void testAddStudentToCourse_Success() {
        studentsService.addStudentToCourse(
                studentMapper.mapToStudentDTO(testStudent), courseMapper.mapToCourseDTO(testCourse)
        );
        testStudent.getCourses().add(testCourse);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        testStudent.getCourses().add(testCourse);
        studentsService.deleteStudentFromCourse(
                studentMapper.mapToStudentDTO(testStudent), courseMapper.mapToCourseDTO(testCourse)
        );
        testStudent.getCourses().remove(testCourse);
        verify(studentRepository, times(1)).save(testStudent);
    }

}