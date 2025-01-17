package com.foxminded.service.impl;

import com.foxminded.dao.CourseDao;
import com.foxminded.dao.GroupDao;
import com.foxminded.dao.StudentDao;
import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.helper.TestDataGenerator;
import com.foxminded.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class DatabaseInitServiceImpl implements DatabaseInitService {

    private final StudentDao studentDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final TestDataGenerator testDataGenerator;
    private final Random random;
    private final Logger logger;

    @Autowired
    public DatabaseInitServiceImpl(StudentDao studentDao,
                                   CourseDao courseDao,
                                   GroupDao groupDao,
                                   TestDataGenerator testDataGenerator) {
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.testDataGenerator = testDataGenerator;
        random = new Random();
        logger = LoggerFactory.getLogger(DatabaseInitServiceImpl.class);
    }

    @Override
    public void init(int coursesAmount, int groupsAmount, int studentsAmount) {
        logger.info("Getting test data from {}", TestDataGenerator.class);
        List<Course> courses = testDataGenerator.generateTestCourses(coursesAmount);
        List<Group> groups = testDataGenerator.generateTestGroups(groupsAmount);
        List<Student> students = testDataGenerator.generateTestStudents(studentsAmount);

        assignCoursesAndGroupsToStudents(students, groups, courses);

        if (courseDao.getCoursesAmount() == 0) {
            logger.info("Saving test courses if courses table is empty");
            courseDao.saveCourses(courses);
        }

        if (groupDao.getGroupsAmount() == 0) {
            logger.info("Saving test groups if groups table is empty");
            groupDao.saveGroups(groups);
        }

        if (studentDao.getStudentsAmount() == 0) {
            logger.info("Saving test students if students table is empty");
            studentDao.saveStudents(students);
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
