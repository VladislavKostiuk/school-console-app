package com.foxminded.ui;

import com.foxminded.domain.Student;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentCourseDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.StudentCourseDTOMapper;
import com.foxminded.enums.CourseName;
import com.foxminded.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static java.util.stream.Collectors.toList;
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
    private List<CourseDTO> testCourses;
    private List<GroupDTO> testGroups;
    private List<StudentDTO> testStudents;
    private Map<Student, Integer> testStudentGroupId;
    private StudentCourseDTOMapper studentCourseMapper;
    private StudentCourseDTO studentWithCourses;

    @BeforeEach
    void setUp() {
        studentCourseMapper = new StudentCourseDTOMapper();

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
                1,
                "firstName1",
                "lastName1"
        );

        StudentDTO student2 = new StudentDTO(
                2,
                1,
                "firstName2",
                "lastName2"
        );

        testStudents = new ArrayList<>(Arrays.asList(student1, student2));
        testStudentGroupId = new HashMap<>();

        StudentDTO student = testStudents.get(0);
        List<String> courseNames = testCourses.stream().map(CourseDTO::name).collect(toList());;
        studentWithCourses = studentCourseMapper.mapToStudentCourseDTO(student, courseNames);
    }

    @Test
    void testFindGroupsByNumber_Success() {
        List<Integer> idList = List.of(1, 2);
        when(studentsService.getGroupIdsByStudentNumber(anyInt())).thenReturn(idList);
        when(groupsService.getGroupsByIds(idList)).thenReturn(testGroups);

        List<GroupDTO> actualGroups = schoolApplication.findGroupsByNumber(25);
        assertEquals(testGroups, actualGroups);
        verify(studentsService, times(1)).getGroupIdsByStudentNumber(25);
        verify(groupsService, times(1)).getGroupsByIds(idList);
    }

    @Test
    void testFindStudentsByCourse_Success() {
        CourseName testCourseName = CourseName.ART;
        when(coursesService.getCourseByName(testCourseName.toString())).thenReturn(testCourses.get(0));
        List<Integer> studentIdList = List.of(1, 2);
        when(studentsCoursesService.getStudentsIdByCourseId(1)).thenReturn(studentIdList);
        when(studentsService.getStudentsByIds(studentIdList)).thenReturn(testStudents);

        List<StudentDTO> actualStudents = schoolApplication.findStudentsByCourse(testCourseName);
        assertEquals(testStudents, actualStudents);
        verify(coursesService, times(1)).getCourseByName(testCourseName.toString());
        verify(studentsCoursesService, times(1)).getStudentsIdByCourseId(1);
        verify(studentsService, times(1)).getStudentsByIds(studentIdList);
    }

    @Test
    void testAddStudent_Success() {
        String groupName = "group1";
        String firstName = "firstName";
        String lastName = "lastName";
        when(groupsService.getAllGroupNames()).thenReturn(
                testGroups.stream().map(GroupDTO::name).collect(toList())
        );

        when(groupsService.getGroupByName(groupName)).thenReturn(testGroups.get(0));
        schoolApplication.addStudent(firstName, lastName, groupName);

        verify(groupsService, times(1)).getGroupByName(groupName);
        verify(studentsService, times(1)).saveStudent(firstName, lastName, testGroups.get(0).id());
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
                testGroups.stream().map(GroupDTO::name).collect(toList())
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
        String testCourseName = CourseName.MEDICINE.toString();
        CourseDTO testCourse = new CourseDTO(
                4,
                CourseName.MEDICINE.toString(),
                "some description"
        );
        when(coursesService.getCourseByName(testCourseName)).thenReturn(testCourse);

        schoolApplication.addStudentToCourse(studentWithCourses, testCourseName);
        verify(coursesService, times(1)).getCourseByName(testCourseName);
        verify(studentsCoursesService, times(1)).addStudentToCourse(1, 4);
    }

    @Test
    void testAddStudentToCourse_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(studentWithCourses, "non-existent course name"));
    }

    @Test
    void testAddStudentToCourse_StudentAlreadyHasThatCourse() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.addStudentToCourse(studentWithCourses, "MATH"));
    }

    @Test
    void testDeleteStudentFromCourse_Success() {
        String testCourseName = CourseName.ART.toString();
        int coursesAmountBeforeDelete = studentWithCourses.courses().size();

        when(coursesService.getCourseByName(testCourseName)).thenReturn(testCourses.get(0));
        when(studentsCoursesService.deleteStudentFromCourse(studentWithCourses.id(), 1)).then(invocationOnMock -> {
            studentWithCourses.courses().remove(1);
            return true;
        });
        assertTrue(schoolApplication.deleteStudentFromCourse(studentWithCourses, testCourseName));
        assertNotEquals(coursesAmountBeforeDelete, studentWithCourses.courses().size());

        verify(coursesService, times(1)).getCourseByName(testCourseName);
        verify(studentsCoursesService, times(1)).deleteStudentFromCourse(studentWithCourses.id(), 1);
    }

    @Test
    void testDeleteStudentFromCourse_CourseWithThatNameDoesNotExist() {
        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(studentWithCourses, "non-existent course name"));

    }

    @Test
    void testDeleteStudentFromCourse_StudentDoesNotHaveCourseWithThatName() {
        String testCourseName = CourseName.MEDICINE.toString();

        assertThrows(IllegalArgumentException.class,
                () -> schoolApplication.deleteStudentFromCourse(studentWithCourses, testCourseName));

    }

    @Test
    void testGetAllGroupNames_Success() {
        List<String> allGroupNames = testGroups.stream().map(GroupDTO::name).collect(toList());
        when(groupsService.getAllGroupNames()).thenReturn(allGroupNames);

        assertEquals(allGroupNames, schoolApplication.getAllGroupNames());
        verify(groupsService, times(1)).getAllGroupNames();
    }

    @Test
    void testGetStudentById_Success() {
        StudentDTO testStudent = testStudents.get(0);
        List<Integer> courseIdList = new ArrayList<>(Arrays.asList(1, 2, 3));

        when(studentsService.getStudentById(1)).thenReturn(testStudent);
        when(studentsCoursesService.getCourseIdsByStudentId(testStudent.id())).thenReturn(courseIdList);
        when(coursesService.getCoursesByIds(courseIdList)).thenReturn(testCourses);

        StudentCourseDTO actualStudent = schoolApplication.getStudentById(1);
        assertEquals(studentWithCourses, actualStudent);
        assertEquals(studentWithCourses.courses(), actualStudent.courses());

        verify(studentsService, times(1)).getStudentById(1);
        verify(studentsCoursesService, times(1)).getCourseIdsByStudentId(testStudent.id());
        verify(coursesService, times(1)).getCoursesByIds(courseIdList);
    }

}