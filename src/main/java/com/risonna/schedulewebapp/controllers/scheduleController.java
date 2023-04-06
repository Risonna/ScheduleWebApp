package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.*;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class scheduleController implements Serializable {
    private List<String> teacherNames =  Arrays.asList("доц. Карабцев С.Н.", "доц. Завозкин С.Ю.", "доц. Фомина Л.Н.", "доц. Бурмин Л.Н.", "доц. Гордиенок Н.И.", "доц. Савельева И.В.", "асс. Илькевич В.В.", "проф. Медведев А.В.", "ст. пр. Тюкалова С.А.", "доц. Гринвальд О.Н.", "доц. Карпинец А.Ю.", "доц. Новосельцева М.А.");
    private List<String> subjects = Arrays.asList("Дифференциальные уравнения", "Дискретная математика", "Программирование", "Физика", "Математический анализ", "Базы данных", "Алгебра", "Введение в специальность", "Иностранный язык", "Циклические виды спорта", "Информатика", "История");
    public List<String> getTeacherNames() {
        return teacherNames;
    }
    public List<String> getSubjects(){
        return subjects;
    }
    public void setSubjects(List<String> subjects){
        this.subjects = subjects;
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
    public void fillSubjects(List<String> subjectNames){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            prepStmt = conn.prepareStatement("INSERT INTO subjects (id, name) VALUES (?, ?)");
            int i = 1;

            for (String subject: subjectNames) {
                prepStmt.setInt(1, i);
                prepStmt.setString(2, subject);
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
    public void fillCabinets(List<String> Cabinets){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            prepStmt = conn.prepareStatement("INSERT INTO cabinets (id, cabinetname, type, seats) VALUES (?, ?, ?, ?)");
            int i = 1;

            for (String cabinetName: Cabinets) {
                prepStmt.setInt(1, i);
                prepStmt.setString(2, cabinetName);
                prepStmt.setString(3, "unknown");
                prepStmt.setString(4, "unknown");
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

    public List<Teacher> getTeacherList() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Teacher> teachers = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM teachers");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String department = rs.getString("department");
                Teacher teacher = new Teacher();
                teacher.setId(id);
                teacher.setTeacherName(name);
                teacher.setDepartment(department);
                teachers.add(teacher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
        return teachers;
    }

    public List<Subject> getSubjectList() {
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Subject> subjects = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM subjects");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Subject subject = new Subject();
                subject.setId(id);
                subject.setSubjectName(name);
                subjects.add(subject);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
        return subjects;
    }
    public List<Cabinet> getCabinetList(){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Cabinet> cabinets = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM cabinets");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("cabinetname");
                String type = rs.getString("type");
                String seats = rs.getString("seats");
                Cabinet cabinet = new Cabinet();
                cabinet.setId(id);
                cabinet.setCabinetName(name);
                cabinet.setSeats(seats);
                cabinet.setType(type);
                cabinets.add(cabinet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
        return cabinets;
    }
    public List<Group> getGroupList(){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Group> groups = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM webschedule.groups;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String fullname = rs.getString("fullname");
                String institute = rs.getString("institute");
                Group group = new Group();
                group.setId(id);
                group.setGroupName(name);
                group.setFullGroupName(fullname);
                group.setInstitute(institute);

                groups.add(group);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
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
        return groups;
    }
    public boolean checkIfValueExists(Connection conn, String tableName, String columnName, String value) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            stmt = conn.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?");
            stmt.setString(1, value);
            rs = stmt.executeQuery();
            rs.next();
            exists = rs.getInt(1) > 0;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }

        return exists;
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
