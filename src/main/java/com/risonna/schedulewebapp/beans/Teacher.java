package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ApplicationScoped
public class Teacher implements Serializable {

    private int id;
    private String title;
    private String teacherName;
    private String teacherSurname;
    private String teacherPatronymic;
    private String department;

    public Teacher(){

    }

    public String getTeacherPatronymic() {
        return teacherPatronymic;
    }

    public String getTeacherSurname() {
        return teacherSurname;
    }

    public void setTeacherPatronymic(String teacherPatronymic) {
        this.teacherPatronymic = teacherPatronymic;
    }

    public void setTeacherSurname(String teacherSurname) {
        this.teacherSurname = teacherSurname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId(){
        return this.id;
    }
    public String getTeacherName(){
        return this.teacherName;
    }

    public String getDepartment(){
        return this.department;
    }

    public void setId(int id){
        this.id = id;
    }
    public void setTeacherName(String teacherName){
        this.teacherName = teacherName;
    }
    public void setDepartment(String department){
        this.department = department;
    }



}
