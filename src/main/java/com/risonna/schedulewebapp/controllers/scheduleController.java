package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.*;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class scheduleController implements Serializable {
    private List<String> teacherNames =  Arrays.asList("unknown", "доц. Карабцев С.Н.", "доц. Завозкин С.Ю.", "доц. Фомина Л.Н.",
            "доц. Бурмин Л.Н.", "доц. Гордиенок Н.И.", "доц. Савельева И.В.", "асс. Илькевич В.В.", "проф. Медведев А.В.",
            "ст. пр. Тюкалова С.А.", "доц. Гринвальд О.Н.", "доц. Карпинец А.Ю.", "доц. Новосельцева М.А.", "проф. Степанов Ю.А.", "доц. Исламов Р.С.",
            "доц. Стуколов С.В.", "доц. Чернова Е.С.", "доц. Власова Н.В.", "проф. Альтшулер О.Г.", "доц. Сергеева О.А.",
            "проф. Рабенко Т.Г.", "доц. Жалнина А.А.", "доц. Буданова Е.А.", "проф. Смоленцев Н.К.", "доц. Чуешев А.В.", "ст.пр. Зимин А.И.",
            "доц. Перевалова А.А.", "доц. Саблинский А.И.", "доц. Зимин А.И.", "доц. Гаврилов О.Ф.", "доц. Мешечкин В.В.", "асс. Степанов И.Ю.",
            "асс. Торгулькин В.В.", "проф. Зиняков Н.М.", "доц. Рейн Т.С.", "работодатель Грибанов А.В.", "доц. Иванов К.С.");
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

    private boolean isCabinetChecked = false;
    private final String[] TIME_PERIODS = {"8.00-9.35", "9.45-11.20", "11.45-13.20", "13.30-15.05", "15.30-17.05", "17.15-18.50"};

    public String[] getTIME_PERIODS() {
        return TIME_PERIODS;
    }

    private String selectedDayOfWeek;
    private String selectedGroup;
    private List<String> daysOfWeek;
    private List<String> groupNames;
    private List<Lesson> filteredLessonsByDayAndGroup;
    private List<Lesson> lessonsByTimePeriodAndGroupAndDay;

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
        for (Lesson lesson : uploadedFileController.lessonsStatic) {
            String groupName = lesson.getGroupName();
            if (!names.contains(groupName)) {
                names.add(groupName);
            }
        }
        return names;
    }


    public List<Lesson> getFilteredLessonsByDayAndGroup(String day) {
        List<Lesson> filteredLessons = new ArrayList<>();
        String selectedGroup = getSelectedGroup();
        if (selectedGroup != null) {
            for (Lesson lesson : uploadedFileController.lessonsStatic) {
                if (lesson.getLessonDay().equalsIgnoreCase(day) && lesson.getGroupName().equalsIgnoreCase(selectedGroup)) {
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
                System.out.println("Filtered lesson: " + lesson.getSubjectName() + lesson.getGroupName() + lesson.getLessonDay() + lesson.getLessonTime());
            }
        }
        System.out.println("Lessons by time period: " + lessonsByTimePeriod);

        return lessonsByTimePeriod;
    }


    private List<Lesson> getLessonsFromSQL(){

        Statement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        List<Lesson> lessonsList = new ArrayList<>();
        try {
            conn = ScheduleDatabase.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM lessons");
            while (rs.next()) {
                int id = rs.getInt("teacherid");
                int subjectid = rs.getInt("subjectid");
                String time = rs.getString("time");
                String day = rs.getString("day");
                int groupId = rs.getInt("groupid");
                int cabinetid = rs.getInt("cabinetid");
                String week = rs.getString("week");
                Lesson lesson = new Lesson();
                lesson.setTeacherId(id);
                lesson.setSubjectId(subjectid);
                lesson.setLessonTime(time);
                lesson.setLessonDay(day);
                lesson.setGroupId(groupId);
                lesson.setCabinetId(cabinetid);
                lesson.setLessonWeek(week);
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
    public ArrayList<Lesson> getLessonsByGroup(ArrayList<Lesson> allLessonList, String group){
        var dayTimeGroupList = new ArrayList<Lesson>();

        for (Lesson lesson: allLessonList) {
            if(lesson.getGroupName().equalsIgnoreCase(group)){
                dayTimeGroupList.add(lesson);
            }

        }
        if(dayTimeGroupList.size() == 0){
            var lessonLesson = new Lesson();
            lessonLesson.setSubjectName("nothing was added");
            lessonLesson.setLessonTime("8.00-9.35");
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
