package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Named
@Table(name = "subjects")
public class Subject {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 250)
    private String subjectName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String name) {
        this.subjectName = name;
    }

    public Subject(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subjects = (Subject) o;
        return id == subjects.id && Objects.equals(subjectName, subjects.subjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectName);
    }
}
