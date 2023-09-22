package com.foxminded.dao.services;

import com.foxminded.CourseName;
import com.foxminded.constants.ErrorMessages;
import com.foxminded.constants.FileNames;
import com.foxminded.dao.CoursesDao;
import com.foxminded.dao.StudentsCoursesDao;
import com.foxminded.dao.factrory.DaoFactory;
import com.foxminded.dao.GroupsDao;
import com.foxminded.dao.StudentsDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.helper.SqlFileScriptExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DaoService {
    private final StudentsDao studentsDao;
    private final CoursesDao coursesDao;
    private final GroupsDao groupsDao;
    private final StudentsCoursesDao studentsCoursesDao;
    private final DaoFactory daoFactory;

    public DaoService() {
        studentsDao = new StudentsDao();
        coursesDao = new CoursesDao();
        groupsDao = new GroupsDao();
        studentsCoursesDao = new StudentsCoursesDao();
        daoFactory = new DaoFactory();
    }

    public void executeStartScript() {
        try (Connection connection = daoFactory.getConnection()) {
            SqlFileScriptExecutor executor = new SqlFileScriptExecutor(connection);
            executor.executeScript(FileNames.CREATE_TABLES_SCRIPT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addStudents(List<Student> students) {
        if (students == null) {
            throw new IllegalArgumentException(ErrorMessages.STUDENTS_CANT_BE_NULL);
        }

        studentsDao.addStudents(students);
        studentsCoursesDao.addSomeStudentsCourses(students);
    }

    public void addGroups(List<Group> groups) {
        if (groups == null) {
            throw new IllegalArgumentException(ErrorMessages.GROUPS_CANT_BE_NULL);
        }

        groupsDao.addGroups(groups);
    }

    public void addCourses(List<Course> courses) {
        if (courses == null) {
            throw new IllegalArgumentException(ErrorMessages.COURSES_CANT_BE_NULL);
        }

        coursesDao.addCourses(courses);
    }

    public List<Group> getGroupsByStudentNumber(int number) {
        List<Integer> groupIdList = studentsDao.getGroupsIdByStudentNumber(number);

        if (groupIdList.isEmpty()) {
            return new ArrayList<>();
        }

        return groupsDao.getGroupsByIds(groupIdList);
    }

    public List<Student> getStudentsByCourseName(CourseName courseName) {
        int courseId = coursesDao.getIdByName(courseName.toString());
        List<Integer> studentsId = studentsCoursesDao.getStudentsIdByCourseId(courseId);
        Map<Student, Integer> studentsGroupIds = studentsDao.getStudentsByIds(studentsId);
        setGroupsToStudents(studentsGroupIds);

        return new ArrayList<>(studentsGroupIds.keySet());
    }

    public void addStudent(String firstName, String lastName, String groupName) {
        int groupId = groupsDao.getGroupByName(groupName).getId();
        studentsDao.addStudent(firstName, lastName, groupId);
    }

    public List<Group> getAllGroups() {
        return groupsDao.getAllGroups();
    }

    public boolean deleteStudentById(int id) {
        studentsCoursesDao.deleteStudentCoursesByStudentId(id);
        return studentsDao.deleteStudentById(id);
    }

    public Student getStudentById(int id) {
        Map<Student, Integer> studentGroupId = studentsDao.getStudentById(id);
        setGroupsToStudents(studentGroupId);
        Student student = new ArrayList<>(studentGroupId.keySet()).get(0);
        List<Integer> courseIds = studentsCoursesDao.getCourseIdsByStudentId(student.getId());
        List<Course> courses = coursesDao.getCoursesByIds(courseIds);
        student.setCourses(courses);
        return student;
    }

    private void setGroupsToStudents(Map<Student, Integer> studentsGroupIds) {
        for (var entry : studentsGroupIds.entrySet()) {
            Group group = groupsDao.getGroupById(entry.getValue());
            Student student = entry.getKey();
            student.setGroup(group);
        }
    }

    public void addStudentToCourse(int studentId, String courseName) {
        int courseId = coursesDao.getIdByName(courseName);
       studentsCoursesDao.addStudentToCourse(studentId, courseId);
    }

    public boolean deleteStudentFromCourse(int studentId, String courseName) {
        int courseId = coursesDao.getIdByName(courseName);
        return studentsCoursesDao.deleteStudentFromCourse(studentId, courseId);
    }
}
