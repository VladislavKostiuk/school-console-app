package com.foxminded.ui;

import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentCourseDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.dto.mappers.CourseDTOMapper;
import com.foxminded.dto.mappers.GroupDTOMapper;
import com.foxminded.dto.mappers.StudentCourseDTOMapper;
import com.foxminded.dto.mappers.StudentDTOMapper;
import com.foxminded.enums.CourseName;
import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class SchoolApplication {

    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsCoursesService studentsCoursesService;
    private final StudentsService studentsService;
    private final CourseDTOMapper courseMapper;
    private final GroupDTOMapper groupMapper;
    private final StudentDTOMapper studentMapper;
    private final StudentCourseDTOMapper studentCourseMapper;

    @Autowired
    public SchoolApplication(CoursesService coursesService,
                             GroupsService groupsService,
                             StudentsCoursesService studentsCoursesService,
                             StudentsService studentsService,
                             DatabaseInitService initService) {
        this.coursesService = coursesService;
        this.groupsService = groupsService;
        this.studentsCoursesService = studentsCoursesService;
        this.studentsService = studentsService;
        initService.init(10, 10, 200);
        courseMapper = new CourseDTOMapper();
        groupMapper = new GroupDTOMapper();
        studentMapper = new StudentDTOMapper();
        studentCourseMapper = new StudentCourseDTOMapper();
    }

    public void showMenu() {
        String menu = "Choose any of proposed options (print the letter): \n" +
                "a) Find all groups with less or equal students’ number\n" +
                "b) Find all students related to the course with the given name\n" +
                "c) Add a new student\n" +
                "d) Delete a student by the STUDENT_ID\n" +
                "e) Add a student to the course (from a list)\n" +
                "f) Remove the student from one of their courses";
        System.out.println(menu);
        executeOptionalAction();
    }

    private void executeOptionalAction() {
        try (Scanner console = new Scanner(System.in)) {
            String option = console.nextLine();
            if (option.equals("a")) {
                printGroupsByNumber(console);
            } else if (option.equals("b")) {
                printStudentsByCourse(console);
            } else if (option.equals("c")) {
                startStudentAdding(console);
            } else if (option.equals("d")) {
                startDeletingStudentById(console);
            } else if (option.equals("e")) {
                startAddingStudentToCourse(console);
            } else if (option.equals("f")) {
                startDeletingStudentFromCourse(console);
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    public List<GroupDTO> findGroupsByNumber(int number) {
        List<Integer> groupIds = studentsService.getGroupIdsByStudentNumber(number);
        return groupIds.isEmpty() ? new ArrayList<>()
                : groupsService.getGroupsByIds(groupIds);
    }

    public List<StudentDTO> findStudentsByCourse(CourseName courseName) {
        CourseDTO course = coursesService.getCourseByName(courseName.toString());
        List<Integer> studentsId = studentsCoursesService.getStudentsIdByCourseId(course.id());
        return studentsService.getStudentsByIds(studentsId);
    }

    public void addStudent(String firstName, String lastName, String groupName) {

        if (firstName == null || lastName == null || firstName.equals("") || lastName.equals("")) {
            throw new IllegalArgumentException(ErrorMessages.WRONG_STUDENT_NAME);
        }

        List<String> allGroupNames = getAllGroupNames();

        if (!allGroupNames.contains(groupName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.GROUP_DOES_NOT_EXIST, groupName));
        }

        GroupDTO group = groupsService.getGroupByName(groupName);
        studentsService.saveStudent(firstName, lastName, group.id());
    }

    public boolean deleteStudentById(int id) {
        studentsCoursesService.deleteStudentCoursesByStudentId(id);
        return studentsService.deleteStudentById(id);
    }

    public void addStudentToCourse(StudentCourseDTO student, String stringCourseName) {
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<String> studentCourses = student.courses();

        if (studentCourses.contains(stringCourseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_ALREADY_HAS_THAT_COURSE, courseName));
        }

        CourseDTO course = coursesService.getCourseByName(courseName.toString());
        studentsCoursesService.addStudentToCourse(student.id(), course.id());
    }

    public boolean deleteStudentFromCourse(StudentCourseDTO student, String stringCourseName) {
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<String> studentCourses = student.courses();

        if (!studentCourses.contains(stringCourseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_DOES_NOT_HAVE_THAT_COURSE, courseName));
        }

        CourseDTO course = coursesService.getCourseByName(courseName.toString());
        return studentsCoursesService.deleteStudentFromCourse(student.id(), course.id());
    }

    public List<String> getAllGroupNames() {
        return groupsService.getAllGroupNames();
    }

    public StudentCourseDTO getStudentById(int id) {
        StudentDTO student = studentsService.getStudentById(id);
        List<Integer> courseIds = studentsCoursesService.getCourseIdsByStudentId(student.id());
        List<String> courses = coursesService.getCoursesByIds(courseIds).stream().map(CourseDTO::name).collect(toList());
        return studentCourseMapper.mapToStudentCourseDTO(student, courses);
    }

    private void printGroupsByNumber(Scanner console) {
        System.out.println("Print students’ number:");
        int number = convertStringToInt(console.nextLine());
        List<GroupDTO> groups = findGroupsByNumber(number);
        printGroups(groups);
    }

    private void printStudentsByCourse(Scanner console) {
        System.out.println("Print course name from the list below: ");
        printAllCourseNames();
        CourseName courseName = convertStringToCourseName(console.nextLine());

        List<StudentDTO> students = findStudentsByCourse(courseName);
        printStudents(students);
    }

    private void startStudentAdding(Scanner console) {
        System.out.println("Print student first name: ");
        String firstName = console.nextLine();
        System.out.println("Print student last name: ");
        String lastName = console.nextLine();
        System.out.println("Print group name (from the list below): ");
        List<String> allGroupNames = getAllGroupNames();
        printGroupNames(allGroupNames);
        String groupName = console.nextLine();

        addStudent(firstName, lastName, groupName);
        System.out.println("Student " + firstName + ", " + lastName + " was added to db");
    }

    private void startDeletingStudentById(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        boolean isDeleted = deleteStudentById(id);

        if (isDeleted) {
            System.out.println("Student with that id was deleted from db");
        } else {
            System.out.println("Student with that id wasn't found");
        }
    }

    private void startAddingStudentToCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        StudentCourseDTO student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nAll available courses:");
        printAllCourseNames();
        System.out.println("\nPrint course that you want to add to this student:");
        String courseName = console.nextLine();

        addStudentToCourse(student, courseName);
        System.out.println(courseName + " was added to students courses list");
    }

    private void startDeletingStudentFromCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        StudentCourseDTO student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nPrint course that you want to remove from this student:");
        String courseName = console.nextLine();

        boolean isDeleted = deleteStudentFromCourse(student, courseName);
        if (isDeleted) {
            System.out.println(courseName + " was deleted from course list of that student");
        }
    }

    private void printCurrentStudentCourses(StudentCourseDTO student) {
        List<String> studentCourses = student.courses();
        System.out.println("Students current courses:");
        printCourseNames(studentCourses);
    }

    private void printGroups(List<GroupDTO> groups) {
        for (var group : groups) {
            System.out.println(group);
        }
    }

    private void printGroupNames(List<String> groupNames) {
        for (var groupName : groupNames) {
            System.out.println(groupName);
        }
    }

    private void printAllCourseNames() {
        for (var courseName : CourseName.values()) {
            System.out.println(courseName);
        }
    }

    private void printStudents (List<StudentDTO> students) {
        for (var student : students) {
            System.out.println(student);
        }
    }

    private void printCourseNames (List<String> courses) {
        for (var course : courses) {
            System.out.println(course);
        }
    }

    private CourseName convertStringToCourseName(String name) {
        try {
            return CourseName.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format(ErrorMessages.COURSE_WAS_NOT_FOUND, name));
        }
    }

    private int convertStringToInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(ErrorMessages.IS_NOT_A_NUMBER, s));
        }
    }

}
