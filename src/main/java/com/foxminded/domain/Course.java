package com.foxminded.domain;

import com.foxminded.enums.CourseName;

public class Course {

    private int id;
    private CourseName name;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CourseName getName() {
        return name;
    }

    public void setName(CourseName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
