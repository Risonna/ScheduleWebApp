package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ApplicationScoped
public class Subject implements Serializable {
    private int id;
    private String subjectName;
    public Subject(){

    }
    public int getId(){
        return this.id;
    }
    public String getSubjectName(){
        return this.subjectName;
    }
    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }
    public void setId(int id){
        this.id = id;
    }
}
