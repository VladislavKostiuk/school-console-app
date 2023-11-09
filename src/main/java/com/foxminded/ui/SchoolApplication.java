package com.foxminded.ui;

import com.foxminded.domain.Group;
import com.foxminded.dto.CourseDTO;
import com.foxminded.dto.GroupDTO;
import com.foxminded.dto.StudentDTO;
import com.foxminded.enums.CourseName;
import com.foxminded.constants.ErrorMessages;
import com.foxminded.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class SchoolApplication {

    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsService studentsService;
    private final Logger logger;

    @Autowired
    public SchoolApplication(CoursesService coursesService,
                             GroupsService groupsService,
                             StudentsService studentsService,
                             DatabaseInitService initService) {
        this.coursesService = coursesService;
        this.groupsService = groupsService;
        this.studentsService = studentsService;
        initService.init(10, 10, 200);
        logger = LoggerFactory.getLogger(SchoolApplication.class);
    }

    public void showMenu() {
        String menu = "Choose any of proposed options (print the letter): \n" +
                "a) Find all groups with less or equal students’ number\n" +
                "b) Find all students related to the course with the given name\n" +
                "c) Add a new student\n" +
                "d) Delete a student by the STUDENT_ID\n" +
                "e) Add a student to the course (from a list)\n" +
                "f) Remove the student from one of their courses";
        logger.info("Printing menu in console");
        System.out.println(menu);
        executeOptionalAction();
    }

    private void executeOptionalAction() {
        logger.info("Getting option from user");
        try (Scanner console = new Scanner(System.in)) {
            String option = console.nextLine();
            logger.info("Option {} is chosen", option);
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
                logger.warn("Option {} is not provided by application", option);
                System.out.println("Unknown option");
            }
        }
    }

    public List<GroupDTO> findGroupsByNumber(int number) {
        logger.info("Start getting groups by student amount {}", number);
        return studentsService.getGroupsByStudentAmount(number);
    }

    public List<StudentDTO> findStudentsByCourse(CourseName courseName) {
        logger.info("Start getting students by course {}", courseName);
        CourseDTO course = coursesService.getCourseByName(courseName);
        return studentsService.getStudentsByCourse(course);
    }

    public void addStudent(String firstName, String lastName, String groupName) {
        logger.info("Start saving student with first name {} and last name {}", firstName, lastName);
        if (firstName == null || lastName == null || firstName.equals("") || lastName.equals("")) {
            throw new IllegalArgumentException(ErrorMessages.WRONG_STUDENT_NAME);
        }

        List<String> allGroupNames = getAllGroupNames();

        if (!allGroupNames.contains(groupName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.GROUP_DOES_NOT_EXIST, groupName));
        }

        GroupDTO group = groupsService.getGroupByName(groupName);
        studentsService.saveStudent(firstName, lastName, group);
    }

    public boolean deleteStudentById(int id) {
        logger.info("Start deleting student by id {}", id);
        return studentsService.deleteStudentById(id);
    }

    public void addStudentToCourse(StudentDTO studentDTO, String stringCourseName) {
        logger.info("Start saving student {} {} to course {}", studentDTO.firstName(), studentDTO.lastName(), stringCourseName);
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<String> studentCourses = studentDTO.courses();

        if (studentCourses.contains(stringCourseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_ALREADY_HAS_THAT_COURSE, courseName));
        }

        CourseDTO courseDTO = coursesService.getCourseByName(courseName);
        studentsService.addStudentToCourse(studentDTO, courseDTO);
    }



    public boolean deleteStudentFromCourse(StudentDTO studentDTO, String stringCourseName) {
        logger.info("Start deleting student {} {} from course {}", studentDTO.firstName(), studentDTO.lastName(), stringCourseName);
        CourseName courseName = convertStringToCourseName(stringCourseName);
        List<String> studentCourses = studentDTO.courses();

        if (!studentCourses.contains(stringCourseName)) {
            throw new IllegalArgumentException(String.format(ErrorMessages.STUDENT_DOES_NOT_HAVE_THAT_COURSE, courseName));
        }

        CourseDTO courseDTO = coursesService.getCourseByName(courseName);
        return studentsService.deleteStudentFromCourse(studentDTO, courseDTO);
    }

    public List<String> getAllGroupNames() {
        logger.info("Start getting all group names");
        return groupsService.getAllGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(int id) {
        logger.info("Start getting student by id");
        return studentsService.getStudentById(id);
    }

    private void printGroupsByNumber(Scanner console) {
        System.out.println("Print students’ number:");
        int number = convertStringToInt(console.nextLine());
        List<GroupDTO> groups = findGroupsByNumber(number);
        logger.info("Printing groups");
        printGroups(groups);
    }

    private void printStudentsByCourse(Scanner console) {
        System.out.println("Print course name from the list below: ");
        printAllCourseNames();
        CourseName courseName = convertStringToCourseName(console.nextLine());

        List<StudentDTO> students = findStudentsByCourse(courseName);
        logger.info("Printing students");
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
        logger.info("Printing the result of adding student");
        System.out.println("Student " + firstName + ", " + lastName + " was added to db");
    }

    private void startDeletingStudentById(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        boolean isDeleted = deleteStudentById(id);

        if (isDeleted) {
            logger.info("Student was deleted successfully");
            System.out.println("Student with that id was deleted from db");
        } else {
            logger.warn("Student was not deleted");
            System.out.println("Student with that id wasn't found");
        }
    }

    private void startAddingStudentToCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        StudentDTO student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nAll available courses:");
        printAllCourseNames();
        System.out.println("\nPrint course that you want to add to this student:");
        String courseName = console.nextLine();

        addStudentToCourse(student, courseName);
        logger.info("Printing the result of adding student to course");
        System.out.println(courseName + " was added to students courses list");
    }

    private void startDeletingStudentFromCourse(Scanner console) {
        System.out.println("Print student id: ");
        int id = convertStringToInt(console.nextLine());
        StudentDTO student = getStudentById(id);

        printCurrentStudentCourses(student);
        System.out.println("\nPrint course that you want to remove from this student:");
        String courseName = console.nextLine();

        boolean isDeleted = deleteStudentFromCourse(student, courseName);
        if (isDeleted) {
            logger.info("Student was deleted from course successfully");
            System.out.println(courseName + " was deleted from course list of that student");
        } else {
            logger.warn("Student was not deleted from course");
        }
    }

    private void printCurrentStudentCourses(StudentDTO student) {
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
