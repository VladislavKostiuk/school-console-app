package com.foxminded.ui;

import com.foxminded.enums.CourseName;
import com.foxminded.constants.ErrorMessages;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.services.*;
import com.foxminded.services.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SchoolApplication {

    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsCoursesService studentsCoursesService;
    private final StudentsService studentsService;

    public SchoolApplication() {
        coursesService = new CoursesServiceImpl();
        groupsService = new GroupsServiceImpl();
        studentsCoursesService = new StudentsCoursesServiceImpl();
        studentsService = new StudentsServiceImpl();
        DatabaseInitService initService = new DatabaseInitServiceImpl();
        initService.init();
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
                findGroupsByNumber(console);
            } else if (option.equals("b")) {
                findStudentsByCourse(console);
            } else if (option.equals("c")) {
                addStudent(console);
            } else if (option.equals("d")) {
                deleteStudentById(console);
            } else if (option.equals("e")) {
                addStudentToCourse(console);
            } else if (option.equals("f")) {
                deleteStudentFromCourse(console);
            } else {
                System.out.println("Unknown option");
            }
        }
    }

    private void findGroupsByNumber(Scanner console) {
        System.out.println("Print students’ number:");
        int amount = convertStringToInt(console.nextLine());
        List<Integer> groupIds = studentsService.getGroupsIdByStudentNumber(amount);
        List<Group> groups = groupIds.isEmpty() ? new ArrayList<>()
                : groupsService.getGroupsByIds(groupIds);
        printGroups(groups);
    }

    private void findStudentsByCourse(Scanner console) {
        System.out.println("Print course name from the list below: ");
        printAllCourseNames();
        CourseName courseName = convertStringToCourseName(console.nextLine());

        int courseId = coursesService.getIdByName(courseName.toString());
        List<Integer> studentsId = studentsCoursesService.getStudentsIdByCourseId(courseId);
        Map<Student, Integer> studentsGroupIds = studentsService.getStudentsByIds(studentsId);
        List<Student> students = groupsService.setGroupToStudents(studentsGroupIds);
        printStudents(students);
    }

    private void addStudent(Scanner console) {
        System.out.println("Print student first name: ");
        String firstName = console.nextLine();
        System.out.println("Print student last name: ");
        String lastName = console.nextLine();

        if (firstName.equals("") || lastName.equals("")) {
            throw new IllegalArgumentException(ErrorMessages.STUDENT_NAME_CANT_BE_EMPTY);
        }

        List<String> allGroupNames = groupsService.getAllGroupNames();
        printGroupNames(allGroupNames);

        String groupName = console.nextLine();

        if (!allGroupNames.contains(groupName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.GROUP_DOES_NOT_EXIST, groupName));
        }

        int groupId = groupsService.getGroupByName(groupName).getId();
        studentsService.saveStudent(firstName, lastName, groupId);
        System.out.println("Student " + firstName + ", " + lastName + " was added to db");
    }

    private void deleteStudentById(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        studentsCoursesService.deleteStudentCoursesByStudentId(id);
        boolean isDeleted = studentsService.deleteStudentById(id);

        if (isDeleted) {
            System.out.println("Student with that id was deleted from db");
        } else {
            System.out.println("Student with that id wasn't found");
        }
    }

    private void addStudentToCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        Map<Student, Integer> studentGroupId = studentsService.getStudentById(id);
        Student student = groupsService.setGroupToStudents(studentGroupId).get(0);
        List<Integer> courseIds = studentsCoursesService.getCourseIdsByStudentId(student.getId());
        List<Course> courses = coursesService.getCoursesByIds(courseIds);
        student.setCourses(courses);

        List<Course> studentCourses = student.getCourses();
        System.out.println("Students current courses:");
        printCourseNames(studentCourses);
        System.out.println("\nAll available courses:");
        printAllCourseNames();
        System.out.println("\nPrint course that you want to add to this student:");
        CourseName courseName = convertStringToCourseName(console.nextLine());
        List<CourseName> courseNames = courses.stream().map(Course::getName).collect(Collectors.toList());

        if (courseNames.contains(courseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_ALREADY_HAS_THAT_COURSE, courseName));
        }

        int courseId = coursesService.getIdByName(courseName.toString());
        studentsCoursesService.addStudentToCourse(student.getId(), courseId);
        System.out.println(courseName + " was added to students courses list");
    }

    private void deleteStudentFromCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        Map<Student, Integer> studentGroupId = studentsService.getStudentById(id);
        Student student = groupsService.setGroupToStudents(studentGroupId).get(0);
        List<Integer> courseIds = studentsCoursesService.getCourseIdsByStudentId(student.getId());
        List<Course> courses = coursesService.getCoursesByIds(courseIds);
        student.setCourses(courses);

        List<Course> studentCourses = student.getCourses();
        System.out.println("Students current courses:");
        printCourseNames(studentCourses);
        System.out.println("\nPrint course that you want to remove from this student:");
        CourseName courseName = convertStringToCourseName(console.nextLine());
        List<CourseName> courseNames = courses.stream().map(Course::getName).collect(Collectors.toList());

        if (!courseNames.contains(courseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_DOES_NOT_HAVE_THAT_COURSE, courseName));
        }

        int courseId = coursesService.getIdByName(courseName.toString());
        boolean isDeleted = studentsCoursesService.deleteStudentFromCourse(student.getId(), courseId);

        if (isDeleted) {
            System.out.println(courseName + " was deleted from course list of that student");
        }
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
