package com.foxminded.services.impl;

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
import com.foxminded.helper.TestDataGenerator;
import com.foxminded.services.*;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DatabaseInitServiceImpl implements DatabaseInitService {

    private final StudentsService studentsService;
    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsCoursesService studentsCoursesService;
    private final DaoFactory daoFactory;
    private final TestDataGenerator testDataGenerator;
    private final Random random;

    public DatabaseInitServiceImpl() {
        studentsService = new StudentsServiceImpl();
        coursesService = new CoursesServiceImpl();
        groupsService = new GroupsServiceImpl();
        studentsCoursesService = new StudentsCoursesServiceImpl();
        daoFactory = new DaoFactory();
        testDataGenerator = new TestDataGenerator();
        random = new Random();
    }

    @Override
    public void init() {
        createTables();

        List<Course> courses = testDataGenerator.generateTestCourses(10);
        List<Group> groups = testDataGenerator.generateTestGroups(10);
        List<Student> students = testDataGenerator.generateTestStudents(200);

        assignCoursesAndGroupsToStudents(students, groups, courses);

        coursesService.saveCourses(courses);
        groupsService.saveGroups(groups);
        studentsService.saveStudents(students);
        studentsCoursesService.saveStudentsCourses(students);
    }

    private void createTables() {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(FileNames.CREATE_TABLES_SCRIPT);
             Reader scriptReader = new InputStreamReader(
                     Optional.ofNullable(inputStream).orElseThrow(() ->
                             new IllegalArgumentException(String.format(
                                     ErrorMessages.FILE_WAS_NOT_FOUND, FileNames.CREATE_TABLES_SCRIPT
                             ))));
             Connection connection = daoFactory.getConnection()) {
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setLogWriter(null);
            scriptRunner.runScript(scriptReader);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void assignCoursesAndGroupsToStudents(List<Student> students, List<Group> groups, List<Course> courses) {
        for (var student : students) {
            Group group = getRandomGroup(groups);
            student.setGroup(group);

            int coursesAmount = random.nextInt(3) + 1;
            for (int j = 0; j < coursesAmount; j++) {
                List<Course> studentCourses = student.getCourses();
                Course randomCourse = getRandomCourse(courses);

                while (studentCourses.contains(randomCourse)) {
                    randomCourse = getRandomCourse(courses);
                }

                studentCourses.add(randomCourse);
            }
        }
    }

    private Group getRandomGroup(List<Group> groups) {
        return groups.get(random.nextInt(groups.size()));
    }

    private Course getRandomCourse(List<Course> courses) {
        return courses.get(random.nextInt(courses.size()));
    }

}
