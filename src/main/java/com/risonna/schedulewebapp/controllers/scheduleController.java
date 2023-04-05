package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.Lesson;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class scheduleController implements Serializable {
    private List<String> teacherNames =  Arrays.asList("доц. Карабцев С.Н.", "доц. Завозкин С.Ю.", "доц. Фомина Л.Н.", "доц. Бурмин Л.Н.", "доц. Гордиенок Н.И.", "доц. Савельева И.В.", "асс. Илькевич В.В.", "проф. Медведев А.В.", "ст. пр. Тюкалова С.А.", "доц. Гринвальд О.Н.", "доц. Карпинец А.Ю.");

    public List<String> getTeacherNames() {
        return teacherNames;
    }

    public void setTeacherNames(List<String> teacherNames) {
        this.teacherNames = teacherNames;
    }

    public scheduleController(){

    }
    public ArrayList<Lesson> dayGroup(ArrayList<Lesson> allLessonList, String day, String group){
        var dayTimeGroupList = new ArrayList<Lesson>();

        for (Lesson lesson: allLessonList) {
            if(lesson.getLessonDay().toLowerCase().equals(day) && lesson.getGroupName().toLowerCase().equals(group)){
                dayTimeGroupList.add(lesson);
            }

        }
        if(dayTimeGroupList.size() == 0){
            var lessonLesson = new Lesson();
            lessonLesson.setSubjectName("nothing was added");
            dayTimeGroupList.add(lessonLesson);
        }
        return dayTimeGroupList;
    }

    public ArrayList<Lesson> dayTeacher(ArrayList<Lesson> allLessonList, String day, String teacher){
        var dayTimeGroupList = new ArrayList<Lesson>();

        for (Lesson lesson: allLessonList) {
            if(lesson.getLessonDay().toLowerCase().equals(day) && lesson.getGroupName().toLowerCase().equals(teacher)){
                dayTimeGroupList.add(lesson);
            }

        }
        if(dayTimeGroupList.size() == 0){
            var lessonLesson = new Lesson();
            lessonLesson.setSubjectName("nothing was added");
            dayTimeGroupList.add(lessonLesson);
        }
        return dayTimeGroupList;
    }

    public void fillTeachers(List<String> teacherNames){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            prepStmt = conn.prepareStatement("INSERT INTO teachers (id, name, department) VALUES (?, ?, ?)");
            int i = 1;

            for (String teacher: teacherNames) {
                prepStmt.setInt(1, i);
                prepStmt.setString(2, teacher);
                prepStmt.setString(3, "don't have that info");
                prepStmt.addBatch();
                i++;

            }
            prepStmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }

        } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }


//    public String updateLessons(ArrayList<Lesson> listWithLessons){
//        PreparedStatement prepStmt = null;
//        ResultSet rs = null;
//        Connection conn = null;
//
//        try {
//            conn = ScheduleDatabase.getConnection();
//            prepStmt = conn.prepareStatement("insert into  (lessons) values (ListWithLessons); set teacher=?, isbn=?, page_count=?, publish_year=?, descr=? where id=?");
//
//
//            for (Lesson lesson : listWithLessons) {
//
//                prepStmt.setString(1, book.getName());
//                prepStmt.setString(2, book.getIsbn());
//                //                prepStmt.setString(3, book.getAuthor());
//                prepStmt.setInt(3, book.getPageCount());
//                prepStmt.setInt(4, book.getPublishDate());
//                //                prepStmt.setString(6, book.getPublisher());
//                prepStmt.setString(5, book.getDescr());
//                prepStmt.setLong(6, book.getId());
//                prepStmt.addBatch();
//            }
//
//
//            prepStmt.executeBatch();
//
//
//        } catch (SQLException ex) {
//            Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (prepStmt != null) {
//                    prepStmt.close();
//                }
//                if (rs != null) {
//                    rs.close();
//                }
//                if (conn != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(BookListController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//
//        return "books";
//
//
//
//        return "Updated";
//    }
}
