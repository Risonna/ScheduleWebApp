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
    private String department;

    public Teacher(){

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
