package com.foxminded.ui;

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

@Component
public class SchoolApplication {

    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsCoursesService studentsCoursesService;
    private final StudentsService studentsService;

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
                printAddingStudent(console);
            } else if (option.equals("d")) {
                printDeletingStudentById(console);
            } else if (option.equals("e")) {
                printAddingStudentToCourse(console);
            } else if (option.equals("f")) {
                printDeletingStudentFromCourse(console);
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    public List<Group> findGroupsByNumber(int number) {
        List<Integer> groupIds = studentsService.getGroupIdsByStudentNumber(number);
        return groupIds.isEmpty() ? new ArrayList<>()
                : groupsService.getGroupsByIds(groupIds);
    }

    public List<Student> findStudentsByCourse(CourseName courseName) {
        int courseId = coursesService.getIdByName(courseName.toString());
        List<Integer> studentsId = studentsCoursesService.getStudentsIdByCourseId(courseId);
        Map<Student, Integer> studentsGroupIds = studentsService.getStudentsByIds(studentsId);
        List<Student> students = groupsService.setGroupToStudents(studentsGroupIds);
        return students;
    }

    public void addStudent(String firstName, String lastName, String groupName) {

        if (firstName == null || lastName == null || firstName.equals("") || lastName.equals("")) {
            throw new IllegalArgumentException(ErrorMessages.WRONG_STUDENT_NAME);
        }

        List<String> allGroupNames = getAllGroupNames();

        if (!allGroupNames.contains(groupName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.GROUP_DOES_NOT_EXIST, groupName));
        }

        int groupId = groupsService.getGroupByName(groupName).getId();
        studentsService.saveStudent(firstName, lastName, groupId);
    }

    public boolean deleteStudentById(int id) {
        studentsCoursesService.deleteStudentCoursesByStudentId(id);
        return studentsService.deleteStudentById(id);
    }

    public void addStudentToCourse(Student student, String stringCourseName) {
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<Course> studentCourses = student.getCourses();
        List<CourseName> courseNames = studentCourses.stream().map(Course::getName).collect(Collectors.toList());

        if (courseNames.contains(courseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_ALREADY_HAS_THAT_COURSE, courseName));
        }

        int courseId = coursesService.getIdByName(courseName.toString());
        studentsCoursesService.addStudentToCourse(student.getId(), courseId);
    }

    public boolean deleteStudentFromCourse(Student student, String stringCourseName) {
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<Course> studentCourses = student.getCourses();
        List<CourseName> courseNames = studentCourses.stream().map(Course::getName).collect(Collectors.toList());

        if (!courseNames.contains(courseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_DOES_NOT_HAVE_THAT_COURSE, courseName));
        }

        int courseId = coursesService.getIdByName(courseName.toString());
        return studentsCoursesService.deleteStudentFromCourse(student.getId(), courseId);
    }

    public List<String> getAllGroupNames() {
        return groupsService.getAllGroupNames();
    }

    public Student getStudentById(int id) {
        Map<Student, Integer> studentGroupId = studentsService.getStudentById(id);
        Student student = groupsService.setGroupToStudents(studentGroupId).get(0);
        List<Integer> courseIds = studentsCoursesService.getCourseIdsByStudentId(student.getId());
        List<Course> courses = coursesService.getCoursesByIds(courseIds);
        student.setCourses(courses);
        return student;
    }

    private void printGroupsByNumber(Scanner console) {
        System.out.println("Print students’ number:");
        int number = convertStringToInt(console.nextLine());
        List<Group> groups = findGroupsByNumber(number);
        printGroups(groups);
    }

    private void printStudentsByCourse(Scanner console) {
        System.out.println("Print course name from the list below: ");
        printAllCourseNames();
        CourseName courseName = convertStringToCourseName(console.nextLine());

        List<Student> students = findStudentsByCourse(courseName);
        printStudents(students);
    }

    private void printAddingStudent(Scanner console) {
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

    private void printDeletingStudentById(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        boolean isDeleted = deleteStudentById(id);

        if (isDeleted) {
            System.out.println("Student with that id was deleted from db");
        } else {
            System.out.println("Student with that id wasn't found");
        }
    }

    private void printAddingStudentToCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        Student student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nAll available courses:");
        printAllCourseNames();
        System.out.println("\nPrint course that you want to add to this student:");
        String courseName = console.nextLine();

        addStudentToCourse(student, courseName);
        System.out.println(courseName + " was added to students courses list");
    }

    private void printDeletingStudentFromCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        Student student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nPrint course that you want to remove from this student:");
        String courseName = console.nextLine();

        boolean isDeleted = deleteStudentFromCourse(student, courseName);
        if (isDeleted) {
            System.out.println(courseName + " was deleted from course list of that student");
        }
    }

    private void printCurrentStudentCourses(Student student) {
        List<Course> studentCourses = student.getCourses();
        System.out.println("Students current courses:");
        printCourseNames(studentCourses);
    }

    private void printGroups(List<Group> groups) {
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

    private void printStudents (List<Student> students) {
        for (var student : students) {
            System.out.println(student);
        }
    }

    private void printCourseNames (List<Course> courses) {
        for (var course : courses) {
            System.out.println(course.getName());
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
