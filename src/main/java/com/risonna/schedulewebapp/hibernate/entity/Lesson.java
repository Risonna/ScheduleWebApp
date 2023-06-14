package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Named
@Table(name = "Lessons")
public class Lesson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "teacherid", nullable = false)
    private int teacherId;
    @Basic
    @Column(name = "subjectid", nullable = false)
    private int subjectId;
    @Basic
    @Column(name = "time", nullable = false, length = 45)
    private String lessonTime;
    @Basic
    @Column(name = "day", nullable = false, length = 45)
    private String lessonDay;
    @Basic
    @Column(name = "groupid", nullable = false)
    private int groupId;
    @Basic
    @Column(name = "cabinetid", nullable = false)
    private int cabinetId;
    @Basic
    @Column(name = "week", nullable = false, length = 45)
    private String lessonWeek;
    @Basic
    @Column(name = "rowfirst", nullable = true)
    private int rowFirst;
    @Basic
    @Column(name = "rowlast", nullable = true)
    private int rowLast;
    @Basic
    @Column(name = "colfirst", nullable = true)
    private int colFirst;
    @Basic
    @Column(name = "collast", nullable = true)
    private int colLast;
    @Basic
    @Column(name = "forwholegroup", nullable = true)
    private boolean forWholeGroup;
    @Basic
    @Column(name = "multiplelessons", nullable = true)
    private boolean multipleLessonsInOneCell;
    @Basic
    @Column(name = "lessonpotoch", nullable = true)
    private boolean potochLesson;
    @Basic
    @Column(name = "groupcolfirst", nullable = true)
    private int groupColFirst;
    @Basic
    @Column(name = "groupcollast", nullable = true)
    private int groupColLast;
    @Basic
    @Column(name = "weekfromweekto", nullable = true, length = 150)
    private String fromWeekToWeek;


    private transient String teacherName;
    private transient String subjectName;
    private transient String cabinetName;
    private transient String groupName;
    private transient String groupNameFull;
    private transient String instituteName;
    private transient String cellId;
    private transient int colSpan;
    private transient int rowSpan;
    private transient boolean lessonCell;
    private transient int groupsForLesson;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectid) {
        this.subjectId = subjectid;
    }

    public String getLessonTime() {
        return lessonTime;
    }

    public void setLessonTime(String time) {
        this.lessonTime = time;
    }

    public String getLessonDay() {
        return lessonDay;
    }

    public void setLessonDay(String day) {
        this.lessonDay = day;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupid) {
        this.groupId = groupid;
    }

    public int getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(int cabinetid) {
        this.cabinetId = cabinetid;
    }

    public String getLessonWeek() {
        return lessonWeek;
    }

    public void setLessonWeek(String week) {
        this.lessonWeek = week;
    }

    public int getRowFirst() {
        return rowFirst;
    }

    public void setRowFirst(int rowfirst) {
        this.rowFirst = rowfirst;
    }

    public int getRowLast() {
        return rowLast;
    }

    public void setRowLast(int rowlast) {
        this.rowLast = rowlast;
    }

    public int getColFirst() {
        return colFirst;
    }

    public void setColFirst(int colfirst) {
        this.colFirst = colfirst;
    }

    public int getColLast() {
        return colLast;
    }

    public void setColLast(int collast) {
        this.colLast = collast;
    }

    public boolean isForWholeGroup() {
        return forWholeGroup;
    }

    public void setForWholeGroup(boolean forwholegroup) {
        this.forWholeGroup = forwholegroup;
    }

    public boolean isMultipleLessonsInOneCell() {
        return multipleLessonsInOneCell;
    }

    public void setMultipleLessonsInOneCell(boolean multiplelessons) {
        this.multipleLessonsInOneCell = multiplelessons;
    }

    public boolean isPotochLesson() {
        return potochLesson;
    }

    public void setPotochLesson(boolean lessonpotoch) {
        this.potochLesson = lessonpotoch;
    }

    public int getGroupColFirst() {
        return groupColFirst;
    }

    public void setGroupColFirst(int groupcolfirst) {
        this.groupColFirst = groupcolfirst;
    }

    public int getGroupColLast() {
        return groupColLast;
    }

    public void setGroupColLast(int groupcollast) {
        this.groupColLast = groupcollast;
    }

    public String getFromWeekToWeek() {
        return fromWeekToWeek;
    }

    public void setFromWeekToWeek(String weekfromweekto) {
        this.fromWeekToWeek = weekfromweekto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lessons = (Lesson) o;
        return id == lessons.id && teacherId == lessons.teacherId && subjectId == lessons.subjectId && groupId == lessons.groupId && cabinetId == lessons.cabinetId && Objects.equals(lessonTime, lessons.lessonTime) && Objects.equals(lessonDay, lessons.lessonDay) && Objects.equals(lessonWeek, lessons.lessonWeek) && Objects.equals(rowFirst, lessons.rowFirst) && Objects.equals(rowLast, lessons.rowLast) && Objects.equals(colFirst, lessons.colFirst) && Objects.equals(colLast, lessons.colLast) && Objects.equals(forWholeGroup, lessons.forWholeGroup) && Objects.equals(multipleLessonsInOneCell, lessons.multipleLessonsInOneCell) && Objects.equals(potochLesson, lessons.potochLesson) && Objects.equals(groupColFirst, lessons.groupColFirst) && Objects.equals(groupColLast, lessons.groupColLast) && Objects.equals(fromWeekToWeek, lessons.fromWeekToWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacherId, subjectId, lessonTime, lessonDay, groupId, cabinetId, lessonWeek, rowFirst, rowLast, colFirst, colLast, forWholeGroup, multipleLessonsInOneCell, potochLesson, groupColFirst, groupColLast, fromWeekToWeek);
    }
    public Lesson(){
        fromWeekToWeek = "";
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupNameFull() {
        return groupNameFull;
    }

    public void setGroupNameFull(String groupNameFull) {
        this.groupNameFull = groupNameFull;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getCellId() {
        return cellId;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public int getColSpan() {
        return colSpan;
    }

    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    public boolean isLessonCell() {
        return lessonCell;
    }

    public void setLessonCell(boolean lessonCell) {
        this.lessonCell = lessonCell;
    }

    public int getGroupsForLesson() {
        return groupsForLesson;
    }

    public void setGroupsForLesson(int groupsForLesson) {
        this.groupsForLesson = groupsForLesson;
    }
    public Lesson(Lesson other) {
        this.id = other.id;
        this.teacherName = other.teacherName;
        this.subjectName = other.subjectName;
        this.cabinetName = other.cabinetName;
        this.lessonDay = other.lessonDay;
        this.lessonTime = other.lessonTime;
        this.groupName = other.groupName;
        this.groupNameFull = other.groupNameFull;
        this.instituteName = other.instituteName;
        this.lessonWeek = other.lessonWeek;
        this.cellId = other.cellId;
        this.fromWeekToWeek = other.fromWeekToWeek;
        this.rowFirst = other.rowFirst;
        this.rowLast = other.rowLast;
        this.colFirst = other.colFirst;
        this.colLast = other.colLast;
        this.forWholeGroup = other.forWholeGroup;
        this.colSpan = other.colSpan;
        this.multipleLessonsInOneCell = other.multipleLessonsInOneCell;
        this.rowSpan = other.rowSpan;
        this.teacherId = other.teacherId;
        this.subjectId = other.subjectId;
        this.cabinetId = other.cabinetId;
        this.groupId = other.groupId;
        this.groupColFirst = other.groupColFirst;
        this.groupColLast = other.groupColLast;
        this.potochLesson = other.potochLesson;
        this.lessonCell = other.lessonCell;
        this.groupsForLesson = other.groupsForLesson;
    }
}
