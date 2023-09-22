package com.foxminded.helper;

import com.foxminded.domain.Course;
import com.foxminded.domain.Group;
import com.foxminded.domain.Student;
import com.foxminded.helper.NamesGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestDataGenerator {
    private final NamesGenerator namesGenerator;
    public TestDataGenerator() {
        namesGenerator = new NamesGenerator();
    }

    public List<Group> generateTestGroups(int amount) {
        List<Group> groups = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            Group group = new Group();
            group.setId(i + 1);
            group.setName(namesGenerator.generateGroupName());
            groups.add(group);
        }

        return groups;
    }

    public List<Course> generateTestCourses(int amount) {
        List<Course> courses = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            Course course = new Course();
            course.setId(i + 1);
            course.setName(namesGenerator.generateCourseName());
            course.setDescription(UUID.randomUUID().toString());
            courses.add(course);
        }

        return courses;
    }

    public List<Student> generateTestStudents(int amount) {
        List<Student> students = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            String[] fullName = namesGenerator.generateStudentFullName();
            Student student = new Student();
            student.setId(i + 1);
            student.setFirstName(fullName[0]);
            student.setLastName(fullName[1]);
            students.add(student);
        }

        return students;
    }
}
