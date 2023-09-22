package com.foxminded.helper;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;

import java.util.List;
import java.util.Random;

public class SchoolAssigner {
    private List<Course> courses;
    private List<Group> groups;
    private List<Student> students;

    private Random random;

    public SchoolAssigner(List<Course> courses, List<Group> groups, List<Student> students) {
        this.courses = courses;
        this.groups = groups;
        this.students = students;
        this.random = new Random();
    }

    public void assignCoursesAndGroupsToStudents() {
        for (var student : students) {
            Group group = getRandomGroup();
            student.setGroup(group);

            int coursesAmount = random.nextInt(3) + 1;
            for (int j = 0; j < coursesAmount; j++) {
                List<Course> studentCourses = student.getCourses();
                Course randomCourse = getRandomCourse();

                while (studentCourses.contains(randomCourse)) {
                    randomCourse = getRandomCourse();
                }

                studentCourses.add(randomCourse);
            }
        }
    }

    private Group getRandomGroup() {
        return groups.get(random.nextInt(groups.size()));
    }

    private Course getRandomCourse() {
        return courses.get(random.nextInt(courses.size()));
    }
}
