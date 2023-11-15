package com.foxminded.ui;

import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.mappers.GroupMapper;
import com.foxminded.enums.CourseName;
import com.foxminded.mappers.GroupMapperImpl;
import com.foxminded.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {
        GroupMapperImpl.class
})
class SchoolApplicationTest {

    private SchoolApplication schoolApplication;
    @Mock
    private CoursesService coursesService;
    @Mock
    private GroupsService groupsService;
    @Mock
    private StudentsService studentsService;
    @Mock
    private DatabaseInitService databaseInitService;
    private List<CourseDTO> testCourses;
    private List<GroupDTO> testGroups;
    private List<StudentDTO> testStudents;
    @Autowired
    private GroupMapper groupMapper;

    @BeforeEach
    void setUp() {
        schoolApplication = new SchoolApplication(
                coursesService, groupsService, studentsService, databaseInitService
        );
        CourseDTO course1 = new CourseDTO(
                1,
                CourseName.ART.toString(),
                "some description"
        );

        CourseDTO course2 = new CourseDTO(
                2,
                CourseName.MATH.toString(),
                "some description"
        );

        CourseDTO course3 = new CourseDTO(
                3,
                CourseName.FINANCE.toString(),
                "some description"
        );

        testCourses = new ArrayList<>(Arrays.asList(course1, course2, course3));

        GroupDTO group1 = new GroupDTO(
                1,
                "group1"
        );

        GroupDTO group2 = new GroupDTO(
                2,
                "group2"
        );

        testGroups = new ArrayList<>(Arrays.asList(group1, group2));

        StudentDTO student1 = new StudentDTO(
                1,
                new GroupDTO(1, "group1"),
                "firstName1",
                "lastName1",
                testCourses
        );

        StudentDTO student2 = new StudentDTO(
                2,
                new GroupDTO(1, "group1"),
                "firstName2",
                "lastName2",
                testCourses
        );

        testStudents = new ArrayList<>(Arrays.asList(student1, student2));
    }

    @Test
    void testFindGroupsByNumber_Success() {
        when(studentsService.getGroupsByStudentAmount(anyInt())).thenReturn(testGroups);
        List<GroupDTO> actualGroups = schoolApplication.findGroupsByNumber(25);
        assertEquals(testGroups, actualGroups);
        verify(studentsService, times(1)).getGroupsByStudentAmount(25);
    }

    @Test
    void testFindStudentsByCourse_Success() {
        CourseName testCourseName = CourseName.ART;
        when(coursesService.getCourseByName(testCourseName)).thenReturn(testCourses.get(0));
        when(studentsService.getStudentsByCourse(testCourses.get(0))).thenReturn(testStudents);

        List<StudentDTO> actualStudents = schoolApplication.findStudentsByCourse(testCourseName);
        assertEquals(testStudents, actualStudents);
        verify(coursesService, times(1)).getCourseByName(testCourseName);
        verify(studentsService, times(1)).getStudentsByCourse(testCourses.get(0));
    }

    @Test
    void testAddStudent_Success() {
        String groupName = "group1";
        String firstName = "firstName";
        String lastName = "lastName";
        when(groupsService.getAllGroups()).thenReturn(
                testGroups.stream().map(groupMapper::mapToGroup).toList()
        );

        when(groupsService.getGroupByName(groupName)).thenReturn(testGroups.get(0));
        schoolApplication.addStudent(firstName, lastName, groupName);

        verify(groupsService, times(1)).getGroupByName(groupName);
        verify(studentsService, times(1)).saveStudent(firstName, lastName, testGroups.get(0));
    }

    @Test
    void testAddStudent_WrongStudentName() {
        String groupName = "group1";

        assertThrows(IllegalArgumentException.class, () -> schoolApplication.addStudent(null, "lastName", groupName));
        assertThrows(IllegalArgumentException.class, () -> schoolApplication.addStudent("firstName", "", groupName));
    }

    @Test
    void testAddStudent_GroupWithThatNameDoesNotExist() {
        String groupName = "non-existent group name";
        String firstName = "firstName";
        String lastName = "lastName";
        when(groupsService.getAllGroups()).thenReturn(
                testGroups.stream().map(groupMapper::mapToGroup).toList()
        );

        assertThrows(IllegalArgumentException.class, () -> schoolApplication.addStudent(firstName, lastName, groupName));
    }

    @Test
    void testDeleteStudentById() {
        int id = 1;
        schoolApplication.deleteStudentById(id);
        verify(studentsService, times(1)).deleteStudentById(id);
    }

    @Test
    void testAddStudentToCourse_Success() {
        CourseName testCourseName = CourseName.MEDICINE;
        CourseDTO testCourse = new CourseDTO(
                4,
                CourseName.MEDICINE.toString(),
                "some description"
        );
        when(coursesService.getCourseByName(testCourseName)).thenReturn(testCourse);

        schoolApplication.addStudentToCourse(testStudents.get(0), CourseName.MEDICINE.toString());
        verify(coursesService, times(1)).getCourseByName(testCourseName);
        verify(studentsService, times(1)).addStudentToCourse(testStudents.get(0), testCourse);
    }

    @Test
    void testAddStudentToCourse_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(testStudents.get(0), "non-existent course name"));
    }

    @Test
    void testAddStudentToCourse_StudentAlreadyHasThatCourse() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(testStudents.get(0), "MATH"));
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        CourseName testCourseName = CourseName.ART;

        when(coursesService.getCourseByName(testCourseName)).thenReturn(testCourses.get(0));
        schoolApplication.deleteStudentFromCourse(testStudents.get(0), testCourseName.toString());

        verify(coursesService, times(1)).getCourseByName(testCourseName);
        verify(studentsService, times(1)).deleteStudentFromCourse(testStudents.get(0), testCourses.get(0));
    }

    @Test
    void testDeleteStudentFromCourse_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(testStudents.get(0), "non-existent course name"));

    }

    @Test
    void testDeleteStudentFromCourse_StudentDoesNotHaveCourseWithThatName() {
        String testCourseName = CourseName.MEDICINE.toString();

        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(testStudents.get(0), testCourseName));

    }

    @Test
    void testGetAllGroupNames_Success() {
        List<String> allGroupNames = testGroups.stream().map(GroupDTO::name).collect(toList());
        when(groupsService.getAllGroups()).thenReturn(testGroups.stream().map(groupMapper::mapToGroup).toList());

        assertEquals(allGroupNames, schoolApplication.getAllGroupNames());
        verify(groupsService, times(1)).getAllGroups();
    }

    @Test
    void testGetStudentById_Success() {
        StudentDTO testStudent = testStudents.get(0);
        when(studentsService.getStudentById(1)).thenReturn(testStudent);
        StudentDTO actualStudent = schoolApplication.getStudentById(1);
        assertEquals(testStudent, actualStudent);
        verify(studentsService, times(1)).getStudentById(1);
    }

}