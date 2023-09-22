package com.foxminded.services;

import com.foxminded.CourseName;
import com.foxminded.dao.services.DaoService;
import com.foxminded.domain.Course;
import com.foxminded.helper.TestDataGenerator;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.helper.SchoolAssigner;

import java.util.List;
import java.util.stream.Collectors;

public class SchoolService {
    private final DaoService daoService;
    private final TestDataGenerator testDataGenerator;

    public SchoolService() {
        daoService = new DaoService();
        testDataGenerator = new TestDataGenerator();

        daoService.executeStartScript();
        initDbWithTestData();
    }

    private void initDbWithTestData() {
        List<Course> courses = testDataGenerator.generateTestCourses(10);
        List<Group> groups = testDataGenerator.generateTestGroups(10);
        List<Student> students = testDataGenerator.generateTestStudents(200);

        SchoolAssigner assigner = new SchoolAssigner(courses, groups, students);
        assigner.assignCoursesAndGroupsToStudents();

        daoService.addCourses(courses);
        daoService.addGroups(groups);
        daoService.addStudents(students);
    }

    public List<Group> getGroupsByStudentNumber(int number) {
        return daoService.getGroupsByStudentNumber(number);
    }
//
    public List<Student> getStudentsByCourseName(CourseName courseName) {
        return daoService.getStudentsByCourseName(courseName);
    }

    public void addStudent(String firstName, String lastName, String groupName) {
        daoService.addStudent(firstName, lastName, groupName);
    }

    public List<String> getAllGroupNames() {
        return daoService.getAllGroups()
                .stream()
                .map(Group::getName)
                .collect(Collectors.toList());
    }

    public boolean deleteStudentById(int id) {
        return daoService.deleteStudentById(id);
    }

    public Student getStudentById(int id) {
        return daoService.getStudentById(id);
    }


    public void addStudentToCourse(Student student, CourseName coursename) {
        daoService.addStudentToCourse(student.getId(), coursename.toString());
    }

    public boolean deleteStudentFromCourse(Student student, CourseName courseName) {
        return daoService.deleteStudentFromCourse(student.getId(), courseName.toString());
    }
}
