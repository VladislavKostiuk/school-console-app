package com.foxminded.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private int id;

    @Column(name = "group_name", nullable = false, unique = true)
    private String name;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group(id: " + id + ", name: " + name + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Group)) {
            return false;
        }

        Group someGroup = (Group) obj;

        if ((this.name == null) ? (someGroup.name != null) : !this.name.equals(someGroup.name)) {
            return false;
        }

        return this.id == someGroup.id;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash += 31 * id;
        hash += 31 * (name != null ? name.hashCode() : 0);
        return hash;
    }

}
