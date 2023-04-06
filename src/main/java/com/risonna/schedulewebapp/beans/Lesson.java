package com.risonna.schedulewebapp.beans;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;


@Named
@ApplicationScoped
public class Lesson implements Serializable {

    private String teacherName;
    private String subjectName;
    private String cabinetName;
    private String lessonDay;
    private String lessonTime;
    private String groupName;
    private String groupNameFull;
    private String instituteName;
    private String lessonWeek;
    private int teacherId;
    private int subjectId;
    private int cabinetId;
    private int groupId;


    public Lesson(){

    }


    //setters
    public void setTeacherName(String teacherName){
        this.teacherName = teacherName;
    }
    public void setSubjectName(String subjectName){
        this.subjectName = subjectName;
    }
    public void setCabinetName(String cabinetName){
        this.cabinetName = cabinetName;
    }
    public void setLessonDay(String lessonDay){
        this.lessonDay = lessonDay;
    }
    public void setLessonTime(String lessonTime){
        this.lessonTime = lessonTime;
    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
    public void setGroupNameFull(String groupNameFull){
        this.groupNameFull = groupNameFull;
    }
    public void setInstituteName(String instituteName){
        this.instituteName = instituteName;
    }

    public void setCabinetId(int cabinetId) { this.cabinetId = cabinetId; }

    public void setGroupId(int groupId) { this.groupId = groupId; }

    public void setSubjectId(int subjectId) { this.subjectId = subjectId; }

    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

    public void setLessonWeek(String lessonWeek) {
        this.lessonWeek = lessonWeek;
    }

    //getters
    public String getTeacherName() {
        return teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public String getLessonDay() {
        return lessonDay;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupNameFull() { return groupNameFull; }

    public String getInstituteName() {
        return instituteName;
    }

    public int getCabinetId() { return cabinetId; }

    public int getGroupId() { return groupId; }

    public int getSubjectId() { return subjectId; }

    public int getTeacherId() { return teacherId; }

    public String getLessonWeek() {
        return lessonWeek;
    }
}
