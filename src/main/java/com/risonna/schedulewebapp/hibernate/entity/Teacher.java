package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Named
@Table(name = "teachers")
public class Teacher {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String teacherName;
    @Basic
    @Column(name = "department", nullable = true, length = 45)
    private String department;
    @Basic
    @Column(name = "title", nullable = true, length = 50)
    private String title;
    @Basic
    @Column(name = "surname", nullable = false, length = 45)
    private String teacherSurname;
    @Basic
    @Column(name = "patronymic", nullable = false, length = 45)
    private String teacherPatronymic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String name) {
        this.teacherName = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTeacherSurname() {
        return teacherSurname;
    }

    public void setTeacherSurname(String surname) {
        this.teacherSurname = surname;
    }

    public String getTeacherPatronymic() {
        return teacherPatronymic;
    }

    public void setTeacherPatronymic(String patronymic) {
        this.teacherPatronymic = patronymic;
    }

    public Teacher(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teachers = (Teacher) o;
        return id == teachers.id && Objects.equals(teacherName, teachers.teacherName) && Objects.equals(department, teachers.department) && Objects.equals(title, teachers.title) && Objects.equals(teacherSurname, teachers.teacherSurname) && Objects.equals(teacherPatronymic, teachers.teacherPatronymic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacherName, department, title, teacherSurname, teacherPatronymic);
    }
}
