package com.foxminded.domain;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private int id;
    private Group group;
    private String firstName;
    private String lastName;
    private List<Course> courses;

    public Student() {
        courses = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Student(id: " + id +
                " group: " + (group != null ? group.getName() : "") +
                ", first name: " + firstName +
                ", last name: " + lastName +
                ")";
    }

}
