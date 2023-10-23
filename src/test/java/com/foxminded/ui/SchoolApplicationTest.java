package com.foxminded.ui;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.enums.CourseName;
import com.foxminded.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolApplicationTest {

    @InjectMocks
    private SchoolApplication schoolApplication;
    @Mock
    private CoursesService coursesService;
    @Mock
    private GroupsService groupsService;
    @Mock
    private StudentsCoursesService studentsCoursesService;
    @Mock
    private StudentsService studentsService;
    @Mock
    private DatabaseInitService databaseInitService;
    private List<Course> testCourses;
    private List<Group> testGroups;
    private List<Student> testStudents;
    private Map<Student, Integer> testStudentGroupId;

    @BeforeEach
    void setUp() {
        Course course1 = new Course();
        course1.setId(1);
        course1.setName(CourseName.ART);

        Course course2 = new Course();
        course2.setId(2);
        course2.setName(CourseName.MATH);

        Course course3 = new Course();
        course3.setId(3);
        course3.setName(CourseName.FINANCE);

        testCourses = new ArrayList<>(Arrays.asList(course1, course2, course3));


        Group group1 = new Group();
        group1.setId(1);
        group1.setName("group1");

        Group group2 = new Group();
        group2.setId(2);
        group2.setName("group2");

        testGroups = new ArrayList<>(Arrays.asList(group1, group2));

        Student student1 = new Student();
        student1.setId(1);
        student1.setGroup(group1);
        student1.setFirstName("firstName1");
        student1.setLastName("lastName1");

        Student student2 = new Student();
        student2.setId(2);
        student2.setGroup(group1);
        student2.setFirstName("firstName2");
        student2.setLastName("lastName2");

        testStudents = new ArrayList<>(Arrays.asList(student1, student2));
        testStudentGroupId = new HashMap<>();
        testStudentGroupId.put(student1, 1);
        testStudentGroupId.put(student2, 1);
    }

    @Test
    void testFindGroupsByNumber_Success() {
        List<Integer> idList = List.of(1, 2);
        when(studentsService.getGroupIdsByStudentNumber(anyInt())).thenReturn(idList);
        when(groupsService.getGroupsByIds(idList)).thenReturn(testGroups);

        List<Group> actualGroups = schoolApplication.findGroupsByNumber(25);
        assertEquals(testGroups, actualGroups);
        verify(studentsService, times(1)).getGroupIdsByStudentNumber(25);
        verify(groupsService, times(1)).getGroupsByIds(idList);
    }

    @Test
    void testFindStudentsByCourse_Success() {
        CourseName testCourseName = CourseName.ART;
        when(coursesService.getIdByName(testCourseName.toString())).thenReturn(1);
        List<Integer> studentIdList = List.of(1, 2);
        when(studentsCoursesService.getStudentsIdByCourseId(1)).thenReturn(studentIdList);
        when(studentsService.getStudentsByIds(studentIdList)).thenReturn(testStudentGroupId);
        when(groupsService.setGroupToStudents(testStudentGroupId)).thenReturn(testStudents);

        List<Student> actualStudents = schoolApplication.findStudentsByCourse(testCourseName);
        assertEquals(testStudents, actualStudents);
        verify(coursesService, times(1)).getIdByName(testCourseName.toString());
        verify(studentsCoursesService, times(1)).getStudentsIdByCourseId(1);
        verify(studentsService, times(1)).getStudentsByIds(studentIdList);
        verify(groupsService, times(1)).setGroupToStudents(testStudentGroupId);
    }

    @Test
    void testAddStudent_Success() {
        String groupName = "group1";
        String firstName = "firstName";
        String lastName = "lastName";
        when(groupsService.getAllGroupNames()).thenReturn(
                testGroups.stream().map(Group::getName).collect(Collectors.toList())
        );

        when(groupsService.getGroupByName(groupName)).thenReturn(testGroups.get(0));
        schoolApplication.addStudent(firstName, lastName, groupName);

        verify(groupsService, times(1)).getGroupByName(groupName);
        verify(studentsService, times(1)).saveStudent(firstName, lastName, testGroups.get(0).getId());
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
        when(groupsService.getAllGroupNames()).thenReturn(
                testGroups.stream().map(Group::getName).collect(Collectors.toList())
        );

        assertThrows(IllegalArgumentException.class, () -> schoolApplication.addStudent(firstName, lastName, groupName));
    }

    @Test
    void testDeleteStudentById() {
        int id = 1;
        when(studentsService.deleteStudentById(id)).thenReturn(true);
        assertTrue(schoolApplication.deleteStudentById(id));
        verify(studentsCoursesService, times(1)).deleteStudentCoursesByStudentId(id);
        verify(studentsService, times(1)).deleteStudentById(id);
    }

    @Test
    void testAddStudentToCourse_Success() {
        Student testStudent = testStudents.get(0);
        testStudent.setCourses(testCourses);
        String testCourseName = CourseName.MEDICINE.toString();
        when(coursesService.getIdByName(testCourseName)).thenReturn(4);

        schoolApplication.addStudentToCourse(testStudent, testCourseName);
        verify(coursesService, times(1)).getIdByName(testCourseName);
        verify(studentsCoursesService, times(1)).addStudentToCourse(testStudent.getId(), 4);
    }

    @Test
    void testAddStudentToCourse_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(testStudents.get(0), "non-existent course name"));
    }

    @Test
    void testAddStudentToCourse_StudentAlreadyHasThatCourse() {
        Student testStudent = testStudents.get(0);
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(testStudent, "non-existent course name"));
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        Student testStudent = testStudents.get(0);
        testStudent.setCourses(testCourses);
        String testCourseName = CourseName.ART.toString();
        int coursesAmountBeforeDelete = testCourses.size();

        when(coursesService.getIdByName(testCourseName)).thenReturn(1);
        when(studentsCoursesService.deleteStudentFromCourse(testStudent.getId(), 1)).then(invocationOnMock -> {
            testStudent.getCourses().remove(1);
            return true;
        });
        assertTrue(schoolApplication.deleteStudentFromCourse(testStudent, testCourseName));
        assertNotEquals(coursesAmountBeforeDelete, testCourses.size());

        verify(coursesService, times(1)).getIdByName(testCourseName);
        verify(studentsCoursesService, times(1)).deleteStudentFromCourse(testStudent.getId(), 1);
    }

    @Test
    void testDeleteStudentFromCourse_CourseWithThatNameDoesNotExist() {
        Student testStudent = testStudents.get(0);
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(testStudent, "non-existent course name"));

    }

    @Test
    void testDeleteStudentFromCourse_StudentDoesNotHaveCourseWithThatName() {
        Student testStudent = testStudents.get(0);
        testStudent.setCourses(testCourses);
        String testCourseName = CourseName.MEDICINE.toString();

        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(testStudent, testCourseName));

    }

    @Test
    void testGetAllGroupNames_Success() {
        List<String> allGroupNames = testGroups.stream().map(Group::getName).collect(Collectors.toList());
        when(groupsService.getAllGroupNames()).thenReturn(allGroupNames);

        assertEquals(allGroupNames, schoolApplication.getAllGroupNames());
        verify(groupsService, times(1)).getAllGroupNames();
    }

    @Test
    void testGetStudentById_Success() {
        Student testStudent = testStudents.get(0);
        List<Integer> courseIdList = new ArrayList<>(Arrays.asList(1, 2, 3));
        testStudentGroupId.remove(testStudents.get(1));

        when(studentsService.getStudentById(1)).thenReturn(testStudentGroupId);
        when(groupsService.setGroupToStudents(testStudentGroupId)).thenReturn(
                new ArrayList<>(Arrays.asList(testStudent))
        );
        when(studentsCoursesService.getCourseIdsByStudentId(testStudent.getId())).thenReturn(courseIdList);
        when(coursesService.getCoursesByIds(courseIdList)).thenReturn(testCourses);

        Student actualStudent = schoolApplication.getStudentById(1);
        assertEquals(testStudent, actualStudent);
        assertEquals(testStudent.getCourses(), actualStudent.getCourses());

        verify(studentsService, times(1)).getStudentById(1);
        verify(groupsService, times(1)).setGroupToStudents(testStudentGroupId);
        verify(studentsCoursesService, times(1)).getCourseIdsByStudentId(testStudent.getId());
        verify(coursesService, times(1)).getCoursesByIds(courseIdList);
    }

}