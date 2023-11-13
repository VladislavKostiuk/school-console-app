package com.foxminded.domain;

import com.foxminded.enums.CourseName;
import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int id;

    @Column(name = "course_name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private CourseName name;

    @Column(name = "course_description")
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if ((!(obj instanceof Course))) {
            return false;
        }

        Course someCourse = (Course) obj;

        if (this.description == null ? (someCourse.description != null)
                : !this.description.equals(someCourse.description)) {
            return false;
        }

        if (this.name == null ? (someCourse.name != null)
                : this.name != someCourse.name) {
            return false;
        }

        return this.id == someCourse.id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += 31 * id;
        hash += 31 * (name != null ? name.hashCode() : 0);
        hash += 31 * (description != null ? description.hashCode() : 0);
        return hash;
    }

}
