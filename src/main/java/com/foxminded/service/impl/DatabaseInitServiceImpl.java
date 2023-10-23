package com.foxminded.service.impl;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.helper.TestDataGenerator;
import com.foxminded.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;

@Service
public class DatabaseInitServiceImpl implements DatabaseInitService {

    private final StudentsService studentsService;
    private final CoursesService coursesService;
    private final GroupsService groupsService;
    private final StudentsCoursesService studentsCoursesService;
    private final TestDataGenerator testDataGenerator;
    private final Random random;

    @Autowired
    public DatabaseInitServiceImpl(StudentsService studentsService,
                                   CoursesService coursesService,
                                   GroupsService groupsService,
                                   StudentsCoursesService studentsCoursesService,
                                   TestDataGenerator testDataGenerator) {
        this.studentsService = studentsService;
        this.coursesService = coursesService;
        this.groupsService = groupsService;
        this.studentsCoursesService = studentsCoursesService;
        this.testDataGenerator = testDataGenerator;
        random = new Random();
    }

    @Override
    public void init(int coursesAmount, int groupsAmount, int studentsAmount) {
        List<Course> courses = testDataGenerator.generateTestCourses(coursesAmount);
        List<Group> groups = testDataGenerator.generateTestGroups(groupsAmount);
        List<Student> students = testDataGenerator.generateTestStudents(studentsAmount);

        assignCoursesAndGroupsToStudents(students, groups, courses);

        if (coursesService.isEmpty()) {
            coursesService.saveCourses(courses);
        }

        if (groupsService.isEmpty()) {
            groupsService.saveGroups(groups);
        }

        if (studentsService.isEmpty()) {
            studentsService.saveStudents(students);
        }

        if (studentsCoursesService.isEmpty()) {
            studentsCoursesService.saveStudentsCourses(students);
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
