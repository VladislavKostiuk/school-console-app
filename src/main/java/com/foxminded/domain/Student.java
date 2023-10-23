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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Student)) {
            return false;
        }

        Student someStudent = (Student) obj;

        if (this.group == null ? someStudent.group != null : !this.group.equals(someStudent.group)) {
            return false;
        }

        if (this.firstName == null ? someStudent.firstName != null : !this.firstName.equals(someStudent.firstName)) {
            return false;
        }

        if (this.lastName == null ? someStudent.lastName != null : !this.lastName.equals(someStudent.lastName)) {
            return false;
        }

        if (this.courses.size() != someStudent.courses.size()) {
            return false;
        }

        for (int i = 0; i < this.courses.size(); i++) {
            if (!this.courses.get(i).equals(someStudent.courses.get(i))) {
                return false;
            }
        }

        return this.id == someStudent.id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += 31 * (group != null ? group.hashCode() : 0);
        hash += 31 * (firstName != null ? firstName.hashCode() : 0);
        hash += 31 * (lastName != null ? lastName.hashCode() : 0);
        hash += 31 * (courses != null ? courses.hashCode() : 0);
        return hash;
    }

}
