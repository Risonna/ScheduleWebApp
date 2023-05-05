package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.*;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

@Named
@ApplicationScoped
public class scheduleController implements Serializable {
    private List<Teacher> teacherList = specifyTheTeachers();

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    private List<String> subjects = Arrays.asList("unknown", "Дифференциальные уравнения", "Дискретная математика",
            "Программирование", "Физика", "Математический анализ", "Базы данных", "Алгебра", "Введение в специальность",
            "Иностранный язык", "Циклические виды спорта", "Информатика", "История", "Высшая математика", "Культура речи и деловое общение",
            "Физическая культура", "Языки программирования", "Теория вероятностей и математическая статистика", "Разработка мобильных приложений",
            "Спортивные игры", "Компьютерные сети", "Введение в финансовую математику", "Технологии параллельного программирования",
            "Социальные коммуникации", "Операционные системы", "Высокоуровневые методы информатики и программирования",
            "Геоинформационные системы", "Разработка web-приложений", "Фитнес", "Проектирование, разработка и оптимизация web-приложений",
            "Численные методы", "Маркетинг", "Особенности программирования в системах реального времени", "Методы оптимизации",
            "Разработка компьютерных игр", "Исследование операций и математическое программирование", "Программирование на JAVA",
            "Математические основы технической кибернетики", "Высокоуровневые методы информатики и программирования",
            "Математическое и имитационное моделирование экономических процессов", "Математические методы обработки экспертной информации",
            "Математические методы оценки инвестиционных проектов", "Программная инженерия", "Исследование операций", "Философия",
            "Глубокое обучение и анализ данных", "Разработка кроссплатформенных мобильных приложений", "Тестирование программного обеспечения",
            "Системы хранения и обработки больших данных", "Разработка нативных мобильных приложений", "Администрирование ERP кластеров",
            "Компьютерное моделирование в современных задачах медицины и промышленн", "Информационная безопасность",
            "Коррупция: причины, проявления, продиводействие", "Разработка мобильных приложений");
    private List<String> cabinets = Arrays.asList("1бл", "2бл", "2130а", "2130б", "2130в", "2131в", "2134", "2139",
            "2141", "2143", "2204", "2210", "2218", "2219", "2220", "2221", "2226бл", "2226", "2229", "5104", "5106", "5109",
            "5113", "5120", "5121", "5320", "5404", "1517", "1313", "1326", "1333", "1335", "4бл", "5бл", "лыжная база 1 корпус");

    private List<Group> groupsFromSQL = getGroupListFromSQL();

    public List<Group> getGroupsFromSQL() {
        return groupsFromSQL;
    }

    public void setGroupsFromSQL(List<Group> groupsFromSQL) {
        this.groupsFromSQL = groupsFromSQL;
    }

    private List<Subject> subjectsFromSQL = getSubjectListFromSQL();

    public List<Subject> getSubjectsFromSQL() {
        return subjectsFromSQL;
    }

    public void setSubjectsFromSQL(List<Subject> subjectsFromSQL) {
        this.subjectsFromSQL = subjectsFromSQL;
    }

    private List<Cabinet> cabinetsFromSQL = getCabinetListFromSQL();

    public List<Cabinet> getCabinetsFromSQL() {
        return cabinetsFromSQL;
    }

    public void setCabinetsFromSQL(List<Cabinet> cabinetsFromSQL) {
        this.cabinetsFromSQL = cabinetsFromSQL;
    }

    private List<Teacher> teachersFromSQL = getTeacherListFromSQL();

    public List<Teacher> getTeachersFromSQL() {
        return teachersFromSQL;
    }

    public void setTeachersFromSQL(List<Teacher> teachersFromSQL) {
        this.teachersFromSQL = teachersFromSQL;
    }

    private List<Lesson> lessonsFromSQL = getLessonsListFromSQL();

    public void setLessonsFromSQL(List<Lesson> lessonsFromSQL) {
        this.lessonsFromSQL = lessonsFromSQL;
    }

    public List<Lesson> getLessonsFromSQL() {
        return lessonsFromSQL;
    }
    private List<Lesson> lessonsOk;

    public List<Lesson> getLessonsOk() {
        lessonsOk = lessonsFromSQL;
        for (Lesson lesson: lessonsOk){
            getSubjectTeacherCabinetGroupById(lesson);
        }
        return lessonsOk;
    }

    public void setLessonsOk(List<Lesson> lessonsOk) {
        this.lessonsOk = lessonsOk;
    }

    public void setCabinets(List<String> cabinets) {
        this.cabinets = cabinets;
    }

    public List<String> getCabinets() {
        return cabinets;
    }

    private boolean isCabinetChecked = false;
    private final String[] TIME_PERIODS = {"8.00-9.35", "9.45-11.20", "11.45-13.20", "13.30-15.05", "15.30-17.05", "17.15-18.50"};

    public String[] getTIME_PERIODS() {
        return TIME_PERIODS;
    }

    private String selectedDayOfWeek;
    private String selectedGroup;
    private String selectedTeacher;
    private List<String> daysOfWeek;
    private List<String> groupNames;
    private List<Lesson> filteredLessonsByDayAndGroup;
    private List<Lesson> lessonsByTimePeriodAndGroupAndDay;
    private List<Lesson> lessonsByTimePeriodAndTeacherAndDay;
    private List<Lesson> listOfStuff;

    public void setListOfStuff(List<Lesson> listOfStuff) {
        this.listOfStuff = listOfStuff;
    }

    public String getSelectedTeacher() {
        return selectedTeacher;
    }
    public void setSelectedTeacher(String selectedTeacher){
        this.selectedTeacher = selectedTeacher;
    }
    public List<String> getTeacherNameList() {
        List<String> names = new ArrayList<>();
        for (Teacher teacher : teachersFromSQL) {
            String teacherName;
            if(!teacher.getTeacherSurname().equalsIgnoreCase("unknown")){
                teacherName = teacher.getTeacherSurname()+" "+teacher.getTeacherName().charAt(0)+"."+teacher.getTeacherPatronymic().charAt(0) + ".";
            }
            else{
                teacherName = teacher.getTeacherName();
            }
            if (!names.contains(teacherName)) {

                names.add(teacherName);
            }
        }
        return names;
    }


    public String getSelectedDayOfWeek() {
        return selectedDayOfWeek;
    }
    public void setSelectedDayOfWeek(String selectedDayOfWeek){
        this.selectedDayOfWeek = selectedDayOfWeek;
    }
    public List<String> getDaysOfWeek() {
        return Arrays.asList("ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА");
    }
    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
    public List<String> getGroupNames() {
        List<String> names = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.prepareStatement("SELECT studentgroups.name FROM `studentgroups`");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String groupName = rs.getString("name");
                names.add(groupName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return names;
    }

    public List<Lesson> getFilteredLessonsByDayAndTeacher(String day) {
        List<Lesson> filteredLessons = new ArrayList<>();
        String selectedTeacher = getSelectedTeacher();
        if (selectedTeacher != null) {
            for (Lesson lesson : getLessonsOk()) {
                if (lesson.getLessonDay().equalsIgnoreCase(day) && lesson.getTeacherName().equalsIgnoreCase(selectedTeacher)) {
                    filteredLessons.add(lesson);
                    System.out.println("getFilteredLessonsByTeacherAndDay " + lesson.getTeacherName());
                }
            }
        }
        return filteredLessons;
    }
    public List<Lesson> getLessonsByTimePeriodAndTeacherAndDay(String timePeriod, String day) {
        List<Lesson> lessonsByDayAndTeacher = getFilteredLessonsByDayAndTeacher(day);
        List<Lesson> lessonsByTimePeriod = new ArrayList<>();
        for (Lesson lesson : lessonsByDayAndTeacher) {
            if (lesson.getLessonTime().contains(timePeriod)) {
                boolean hasLessonWithDayTime = false;
                if(!lessonsByTimePeriod.isEmpty()) {
                    for (Lesson lessonByTime : lessonsByTimePeriod) {
                        if (lessonByTime.getLessonTime().equalsIgnoreCase(lesson.getLessonTime()) &&
                                lessonByTime.getLessonDay().equalsIgnoreCase(lesson.getLessonDay()) &&
                                lessonByTime.getLessonWeek().equalsIgnoreCase(lesson.getLessonWeek())) {
                            hasLessonWithDayTime = true;
                            break;
                        }
                    }
                }
                if(!hasLessonWithDayTime){
                    lessonsByTimePeriod.add(lesson);
                }
                System.out.println("getLessonsByTimePeriodAndTeacherAndDay: " + lesson.getSubjectName() + lesson.getGroupName() + lesson.getLessonDay() + lesson.getLessonTime());
            }
        }
        System.out.println("Lessons by time period getLessonsByTimePeriodAndTeacherAndDay: " + lessonsByTimePeriod);

        return lessonsByTimePeriod;
    }

    public List<Lesson> getFilteredLessonsByDayAndGroup(String day) {
        List<Lesson> filteredLessons = new ArrayList<>();
        String selectedGroup = getSelectedGroup();
        if (selectedGroup != null) {
            for (Lesson lesson : getLessonsOk()) {
                if (lesson.getLessonDay().equalsIgnoreCase(day) && lesson.getGroupName().trim().equalsIgnoreCase(selectedGroup.trim())) {

                    filteredLessons.add(lesson);
                }
            }
        }
        return filteredLessons;
    }

    public List<Lesson> getLessonsByTimePeriodAndGroupAndDay(String timePeriod, String day) {
        List<Lesson> lessonsByDayAndGroup = getFilteredLessonsByDayAndGroup(day);
        List<Lesson> lessonsByTimePeriod = new ArrayList<>();
        for (Lesson lesson : lessonsByDayAndGroup) {
            if (lesson.getLessonTime().contains(timePeriod)) {
                lessonsByTimePeriod.add(lesson);
            }
        }

        return lessonsByTimePeriod;
    }

    public List<List<Lesson>> getListsStuff(List<Lesson> listHuh) {
        List<Integer> rowPositions = new ArrayList<>();
        int rowAmount = 0;
        for (Lesson lesson : listHuh) {
            if (!rowPositions.contains(lesson.getRowNum())) {
                rowPositions.add(lesson.getRowNum());
                rowAmount++;
            }
        }

        List<List<Lesson>> listOfListsOfLessons = new ArrayList<>();
        for (int j = 0; j < rowAmount; j++) {
            List<Lesson> listOfLessons = new ArrayList<>();
            int rownum = rowPositions.get(j);
            for (Lesson lesson : listHuh) {
                if (lesson.getRowNum() == rownum) {
                    listOfLessons.add(lesson);
                }
            }
            System.out.println("new teach is: " + listOfLessons.get(0).getTeacherName());
            listOfListsOfLessons.add(listOfLessons);
        }

        for (List<Lesson> lessonList:listOfListsOfLessons) {
            for (Lesson lesson:lessonList) {


            }

        }
        return listOfListsOfLessons;
    }




    private List<Lesson> getLessonsListFromSQL(){

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
                int rowNum = rs.getInt("rownum");
                Lesson lesson = new Lesson();
                lesson.setTeacherId(teacherid);
                lesson.setSubjectId(subjectid);
                lesson.setLessonTime(time);
                lesson.setLessonDay(day);
                lesson.setGroupId(groupId);
                lesson.setCabinetId(cabinetid);
                lesson.setLessonWeek(week);
                lesson.setRowNum(rowNum);
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

    private List<String> listOfRepeatedCabinets = new ArrayList<>();

    public List<String> getListOfRepeatedCabinets() {
        return listOfRepeatedCabinets;
    }

    public void setListOfRepeatedCabinets(List<String> listOfRepeatedCabinets) {
        this.listOfRepeatedCabinets = listOfRepeatedCabinets;
    }

    public boolean isCabinetChecked() {
        return isCabinetChecked;
    }

    public void setCabinetChecked(boolean cabinetChecked) {
        isCabinetChecked = cabinetChecked;
    }

    public List<String> getSubjects(){
        return subjects;
    }
    public void setSubjects(List<String> subjects){
        this.subjects = subjects;
    }

    public scheduleController() {


    }

    private List<Teacher> specifyTheTeachers(){
        List<Teacher> teacherlist = new ArrayList<>();
        List<String> teachers = Arrays.asList("unknown",
                "доц. Гордиенок Н.И.", "доц. Савельева И.В.",
                "ст. пр. Тюкалова С.А.", "доц. Гринвальд О.Н.", "доц. Карпинец А.Ю.", "доц. Новосельцева М.А.", "доц. Исламов Р.С.",
                "доц. Чернова Е.С.", "доц. Власова Н.В.", "проф. Альтшулер О.Г.", "доц. Сергеева О.А.",
                "проф. Рабенко Т.Г.", "доц. Жалнина А.А.", "доц. Буданова Е.А.", "проф. Смоленцев Н.К.", "доц. Чуешев А.В.",
                "доц. Гаврилов О.Ф.",
                "асс. Торгулькин В.В.", "проф. Зиняков Н.М.", "доц. Рейн Т.С.", "работодатель Грибанов А.В.");

        List<String> teacherNames = Arrays.asList("unknown",
                "Гордиенок Н.И.", "Савельева И.В.",
                "Тюкалова С.А.", "Гринвальд О.Н.", "Карпинец А.Ю.", "Новосельцева М.А.", "Исламов Р.С.",
                "Чернова Е.С.", "Власова Н.В.", "Альтшулер О.Г.", "Сергеева О.А.",
                "Рабенко Т.Г.", "Жалнина А.А.", "Буданова Е.А.", "Смоленцев Н.К.", "Чуешев А.В.", "Гаврилов О.Ф.",
                "Торгулькин В.В.", "Зиняков Н.М.", "Рейн Т.С.",
                "Фролова Т.В.", "Каган Е.С.", "Инденко О.Н.", "Борисов В.Г.",
                "Рыкова Н.Ф.", "Напреенко Г.В.", "Григорик Н.Н.", "Нятина Н.В.", "Гутова С.Г.",
                "Кучер Н.А.", "Дмитриева Н.В.", "Тимофеева Н.А.", "Шаров А.А.", "Борисова М.В.",
                "Скотникова Л.Н.");

        List<String> teacherTitles = Arrays.asList("unknown",
                "доц", "доц",
                "ст. пр.", "доц", "доц", "доц", "проф", "доц",
                "доц", "доц",
                "проф", "доц", "доц", "проф", "доц",
                "доц", "доц", "доц", "асс",
                "проф", "доц",
                "работодатель", "доц", "ст. пр.", "работодатель", "доц", "ст. пр.", "доц",
                "доц", "доц", "доц", "асс", "асс", "ст. пр.", "ст. пр.");

        for (int i = 0; i<teacherNames.size(); i++){
            Teacher teacher = new Teacher();
            teacher.setTeacherName(teacherNames.get(i));
            teacher.setDepartment("unknown");
            teacher.setTitle(teacherTitles.get(i));
            teacher.setTeacherSurname("unknown");
            teacher.setTeacherPatronymic("unknown");
            teacherlist.add(teacher);
        }

        return teacherlist;
    }


    public void fillTeachers(List<Teacher> teacherNames){
        PreparedStatement prepStmt = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = ScheduleDatabase.getConnection();
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * from teachers");
            int i = 1;
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
            prepStmt = conn.prepareStatement("INSERT INTO lessons (id, teacherid, subjectid, time, day, groupid, cabinetid, week, rownum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
                prepStmt.setInt(9, lesson.getRowNum());
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
            rs = stmt.executeQuery("SELECT * FROM webschedule.studentgroups;");
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

    public List<String> checkCabinetUsage(List<Lesson> allLessons) {
        List<String> repeatedCabinetUse = new ArrayList<>();
        Set<String> seenCabinetUses = new HashSet<>(); // Keep track of identified cabinet usage combinations
        for (int i = 0; i < allLessons.size(); i++) {
            Lesson lesson = allLessons.get(i);
            String cabinetToCheck = lesson.getCabinetName();
            String cabinetTime = lesson.getLessonTime();
            String cabinetDay = lesson.getLessonDay();
            for (int j = i + 1; j < allLessons.size(); j++) { // Only check the remaining lessons in the list
                Lesson lessonj = allLessons.get(j);
                if (lesson.getLessonWeek().equals(lessonj.getLessonWeek()) &&
                        cabinetToCheck.equals(lessonj.getCabinetName()) &&
                        cabinetTime.equals(lessonj.getLessonTime()) &&
                        cabinetDay.equals(lessonj.getLessonDay())) {
                    String cabinetUse = "Cabinet " + cabinetToCheck + " is in use on " +
                            cabinetTime + cabinetDay + " by groups " + lessonj.getGroupName() +
                            " and " + lesson.getGroupName();
                    if (!seenCabinetUses.contains(cabinetUse)) {
                        repeatedCabinetUse.add(cabinetUse);
                        seenCabinetUses.add(cabinetUse);
                    }
                }
            }
        }
        setCabinetChecked(true);
        listOfRepeatedCabinets = repeatedCabinetUse;
        return repeatedCabinetUse;
    }

    public void getSubjectTeacherCabinetGroupById(Lesson lesson){
        lesson.setGroupName("unknown");
        for(Group group: groupsFromSQL){
            if(lesson.getGroupId() == group.getId()){
                lesson.setGroupName(group.getGroupName());
                break;
            }
        }
        lesson.setSubjectName("unknown");
        for(Subject subject: subjectsFromSQL){
            if(lesson.getSubjectId() == subject.getId()){
                lesson.setSubjectName(subject.getSubjectName());
                break;
            }
        }
        lesson.setCabinetName("unknown");
        for(Cabinet cabinet: cabinetsFromSQL){
            if(lesson.getCabinetId() == cabinet.getId()){
                lesson.setCabinetName(cabinet.getCabinetName());
                break;
            }
        }
        lesson.setTeacherName("unknown");
        for(Teacher teacher: teachersFromSQL){
            if(lesson.getTeacherId() == teacher.getId()){
                if(!teacher.getTeacherSurname().equalsIgnoreCase("unknown")){
                    lesson.setTeacherName(teacher.getTeacherSurname()+" "+teacher.getTeacherName().charAt(0)+"."+teacher.getTeacherPatronymic().charAt(0)+".");
                }
                else{
                    lesson.setTeacherName(teacher.getTeacherName());
                }
                 break;
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
