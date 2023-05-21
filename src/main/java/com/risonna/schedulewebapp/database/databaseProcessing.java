package com.risonna.schedulewebapp.database;

import com.risonna.schedulewebapp.beans.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class databaseProcessing implements Serializable {

    public databaseProcessing(){

    }

    public List<Lesson> getLessonsListFromSQL(){

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Lesson> lessonsList = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM lessons");
            while (rs.next()) {
                int teacherid = rs.getInt("teacherid");
                int subjectid = rs.getInt("subjectid");
                String time = rs.getString("time");
                String day = rs.getString("day");
                int groupId = rs.getInt("groupid");
                int cabinetid = rs.getInt("cabinetid");
                String week = rs.getString("week");
                int rowFirst = rs.getInt("rowfirst");
                int rowLast = rs.getInt("rowlast");
                int colFirst = rs.getInt("colfirst");
                int colLast = rs.getInt("collast");
                boolean forWholeGroup = rs.getBoolean("forwholegroup");
                boolean multipleLessonsInOneCell = rs.getBoolean("multiplelessons");
                boolean potochLesson = rs.getBoolean("lessonpotoch");
                int groupColFirst = rs.getInt("groupcolfirst");
                int groupColLast = rs.getInt("groupcollast");
                Lesson lesson = new Lesson();
                lesson.setTeacherId(teacherid);
                lesson.setSubjectId(subjectid);
                lesson.setLessonTime(time);
                lesson.setLessonDay(day);
                lesson.setGroupId(groupId);
                lesson.setCabinetId(cabinetid);
                lesson.setLessonWeek(week);
                lesson.setRowFirst(rowFirst);
                lesson.setRowLast(rowLast);
                lesson.setColFirst(colFirst);
                lesson.setColLast(colLast);
                lesson.setForWholeGroup(forWholeGroup);
                lesson.setMultipleLessonsInOneCell(multipleLessonsInOneCell);
                lesson.setPotochLesson(potochLesson);
                lesson.setGroupColFirst(groupColFirst);
                lesson.setGroupColLast(groupColLast);
                lessonsList.add(lesson);
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

        return lessonsList;
    }
    public List<Teacher> getTeacherListFromSQL() {
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
                String title = rs.getString("title");
                String surname = rs.getString("surname");
                String patronymic = rs.getString("patronymic");
                Teacher teacher = new Teacher();
                teacher.setId(id);
                teacher.setTeacherName(name);
                teacher.setDepartment(department);
                teacher.setTitle(title);
                teacher.setTeacherSurname(surname);
                teacher.setTeacherPatronymic(patronymic);
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
    public List<Subject> getSubjectListFromSQL() {
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
    public List<Cabinet> getCabinetListFromSQL(){
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
    public List<Group> getGroupListFromSQL(){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Group> groups = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM webschedule.studentgroups");
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
    public List<User> getUsersFromSQL(){
        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<User> users = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM webschedule.users");
            while (rs.next()) {
                String userid = rs.getString("userid");
                String email = rs.getString("email");
                String creation_time = rs.getString("creation_time");
                User user = new User();
                user.setUsername(userid);
                user.setEmail(email);
                user.setRegistration_time(creation_time);
                users.add(user);
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

        return users;
    }

    public void fillTeachers(List<Teacher> teacherNames){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * from teachers");
            int i = 0;
            while (rs.next()){
                i = rs.getInt("id") + 1;
            }
            prepStmt = conn.prepareStatement("INSERT INTO teachers (id, name, department, title, surname, patronymic) VALUES (?, ?, ?, ?, ?, ?)");
            for (Teacher teacher: teacherNames) {
                prepStmt.setInt(1, i);
                prepStmt.setString(2, teacher.getTeacherName());
                prepStmt.setString(3, teacher.getDepartment());
                prepStmt.setString(4, teacher.getTitle());
                prepStmt.setString(5, teacher.getTeacherSurname());
                prepStmt.setString(6, teacher.getTeacherPatronymic());
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
            int i = 0;

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
            prepStmt.setInt(1, 0);
            prepStmt.setString(2, "unknown");
            prepStmt.setString(3, "unknown");
            prepStmt.setString(4, "unknown");
            prepStmt.addBatch();
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
    public void fillLessons(List<Lesson> lessons){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            prepStmt = conn.prepareStatement("INSERT INTO lessons (id, teacherid, subjectid, time, day, groupid, cabinetid, week, " +
                    "rowfirst, rowlast, colfirst, collast, forwholegroup, multiplelessons, lessonpotoch, groupcolfirst, groupcollast) VALUES" +
                    " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            int i = 1;
            for (Lesson lesson: lessons) {
                prepStmt.setInt(1, i);
                prepStmt.setInt(2, lesson.getTeacherId());
                prepStmt.setInt(3, lesson.getSubjectId());
                prepStmt.setString(4, lesson.getLessonTime());
                prepStmt.setString(5, lesson.getLessonDay());
                prepStmt.setInt(6, lesson.getGroupId());
                prepStmt.setInt(7, lesson.getCabinetId());
                prepStmt.setString(8, lesson.getLessonWeek());
                prepStmt.setInt(9, lesson.getRowFirst());
                prepStmt.setInt(10, lesson.getRowLast());
                prepStmt.setInt(11, lesson.getColFirst());
                prepStmt.setInt(12, lesson.getColLast());
                prepStmt.setBoolean(13, lesson.isForWholeGroup());
                prepStmt.setBoolean(14, lesson.isMultipleLessonsInOneCell());
                prepStmt.setBoolean(15, lesson.isPotochLesson());
                prepStmt.setInt(16, lesson.getGroupColFirst());
                prepStmt.setInt(17, lesson.getGroupColLast());
                prepStmt.addBatch();
                i++;

            }
            System.out.println(lessons.size());
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
    public void fillGroups(List<String> groups){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            prepStmt = conn.prepareStatement("INSERT INTO studentgroups (id, name, fullname, institute) VALUES (?, ?, ?, ?)");
            prepStmt.setInt(1, 0);
            prepStmt.setString(2, "unknown");
            prepStmt.setString(3, "unknown");
            prepStmt.setString(4, "unknown");
            prepStmt.addBatch();
            int i = 1;
            for (String groupName: groups) {
                prepStmt.setInt(1, i);
                prepStmt.setString(2, groupName);
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

}
