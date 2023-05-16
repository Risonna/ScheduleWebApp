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
    private String cellId;
    private int rowFirst;
    private int rowLast;
    private int colFirst;
    private int colLast;
    private boolean forWholeGroup;
    private int colSpan;
    private boolean multipleLessonsInOneCell;
    private int teacherId;
    private int subjectId;
    private int cabinetId;
    private int groupId;
    private boolean potochLesson;
    private boolean lessonCell;
    private int groupsForLesson;


    public Lesson(){

    }


    //setters






    public int getGroupsForLesson() {
        return groupsForLesson;
    }

    public void setGroupsForLesson(int groupsForLesson) {
        this.groupsForLesson = groupsForLesson;
    }

    public boolean isLessonCell() {
        return lessonCell;
    }

    public void setLessonCell(boolean lessonCell) {
        this.lessonCell = lessonCell;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public boolean isPotochLesson() {
        return potochLesson;
    }

    public void setPotochLesson(boolean potochLesson) {
        this.potochLesson = potochLesson;
    }

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

    public String getCellId() {
        return cellId;
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

    public int getRowFirst() {
        return rowFirst;
    }

    public void setRowFirst(int rowFirst) {
        this.rowFirst = rowFirst;
    }

    public int getRowLast() {
        return rowLast;
    }

    public void setRowLast(int rowLast) {
        this.rowLast = rowLast;
    }

    public int getColFirst() {
        return colFirst;
    }

    public void setColFirst(int colFirst) {
        this.colFirst = colFirst;
    }

    public int getColLast() {
        return colLast;
    }

    public void setColLast(int colLast) {
        this.colLast = colLast;
    }

    public boolean isForWholeGroup() {
        return forWholeGroup;
    }

    public void setForWholeGroup(boolean forWholeGroup) {
        this.forWholeGroup = forWholeGroup;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public boolean isMultipleLessonsInOneCell() {
        return multipleLessonsInOneCell;
    }

    public void setMultipleLessonsInOneCell(boolean multipleLessonsInOneCell) {
        this.multipleLessonsInOneCell = multipleLessonsInOneCell;
    }
}
