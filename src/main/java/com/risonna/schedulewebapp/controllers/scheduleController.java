package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.*;
import com.risonna.schedulewebapp.database.databaseProcessing;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.Serializable;
import java.sql.*;
import java.util.*;

@Named
@SessionScoped
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

    private List<String> departmentsFromSQL;

    public void setDepartmentsFromSQL(List<String> departmentsFromSQL) {
        this.departmentsFromSQL = departmentsFromSQL;
    }

    public List<String> getDepartmentsFromSQL() {
        return departmentsFromSQL;
    }

    private List<Group> groupsFromSQL;

    public List<Group> getGroupsFromSQL() {
        return groupsFromSQL;
    }

    public void setGroupsFromSQL(List<Group> groupsFromSQL) {
        this.groupsFromSQL = groupsFromSQL;
    }

    private List<Subject> subjectsFromSQL;

    public List<Subject> getSubjectsFromSQL() {
        return subjectsFromSQL;
    }

    public void setSubjectsFromSQL(List<Subject> subjectsFromSQL) {
        this.subjectsFromSQL = subjectsFromSQL;
    }

    private List<Cabinet> cabinetsFromSQL;

    public List<Cabinet> getCabinetsFromSQL() {
        return cabinetsFromSQL;
    }

    public void setCabinetsFromSQL(List<Cabinet> cabinetsFromSQL) {
        this.cabinetsFromSQL = cabinetsFromSQL;
    }

    private List<Teacher> teachersFromSQL;

    public List<Teacher> getTeachersFromSQL() {
        return teachersFromSQL;
    }

    public void setTeachersFromSQL(List<Teacher> teachersFromSQL) {
        this.teachersFromSQL = teachersFromSQL;
    }

    private List<Lesson> lessonsFromSQL;

    public void setLessonsFromSQL(List<Lesson> lessonsFromSQL) {
        this.lessonsFromSQL = lessonsFromSQL;
    }

    public List<Lesson> getLessonsFromSQL() {
        return lessonsFromSQL;
    }
    private List<Lesson> lessonsOk;

    public List<Lesson> getLessonsOk() {
        lessonsOk = getLessonsFromSQL();


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
    private final String[] TIME_PERIODS = {"8.00-9.35", "9.45-11.20", "11.45-13.20", "13.30-15.05", "15.30-17.05", "17.15-18.50", "19.00-20.35"};

    public String[] getTIME_PERIODS() {
        return TIME_PERIODS;
    }

    private String selectedGroup;
    private String selectedTeacher;
    private String selectedDayOfWeek;
    private String selectedDepartment;
    private List<String> daysOfWeek;

    private List<String> groupNames;
    private List<String> departmentList;
    private List<Lesson> filteredLessonsByDayAndGroup;
    private List<Lesson> lessonsByTimePeriodAndGroupAndDay;
    private List<Lesson> lessonsByTimePeriodAndTeacherAndDay;
    private List<Lesson> listOfStuff;

    public void setListOfStuff(List<Lesson> listOfStuff) {
        this.listOfStuff = listOfStuff;
    }

    public String getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(String selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public String getSelectedTeacher() {
        return selectedTeacher;
    }
    public void setSelectedTeacher(String selectedTeacher){
        this.selectedTeacher = selectedTeacher;
    }
    public List<String> getTeacherNameList() {
        List<String> names = new ArrayList<>();
        for (Teacher teacher : getTeachersFromSQL()) {
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


    public List<String> getTeachersForDepartment(){
        List<String> names = new ArrayList<>();
        names.add("Показать Всех");
        for (Teacher teacher:teachersFromSQL) {
            if(teacher.getDepartment().equalsIgnoreCase(selectedDepartment)){
                if(!teacher.getDepartment().equalsIgnoreCase("unknown"))
                names.add(teacher.getTeacherSurname()+" "+teacher.getTeacherName().charAt(0)+"."+teacher.getTeacherPatronymic().charAt(0) + ".");
                else names.add(teacher.getTeacherName());
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
        return Arrays.asList("ПОНЕДЕЛЬНИК", "ВТОРНИК", "СРЕДА", "ЧЕТВЕРГ", "ПЯТНИЦА", "СУББОТА", "ВОСКРЕСЕНЬЕ", "ВСЯ НЕДЕЛЯ");
    }
    public List<String> getDepartmentList(){
        List<String> listOfDepartments = new ArrayList<>();
        for (Teacher teacher:teachersFromSQL) {
            if(!listOfDepartments.contains(teacher.getDepartment())){
                listOfDepartments.add(teacher.getDepartment());
            }

        }
        return listOfDepartments;
    }
    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }
    public List<String> getGroupNames() {
        List<String> groupNames = new ArrayList<>();
        for(Group group:getGroupsFromSQL()){
            groupNames.add(group.getGroupName());
        }
        return groupNames;
    }


    public List<Lesson> getFilteredLessonsByDayAndTeacher(String day, String teacherName) {
        List<Lesson> filteredLessons = new ArrayList<>();
        if (teacherName != null) {
            for (Lesson lesson : getLessonsOk()) {
                if (lesson.getLessonDay().equalsIgnoreCase(day) && lesson.getTeacherName().equalsIgnoreCase(teacherName)) {
                    filteredLessons.add(lesson);
                    System.out.println("getFilteredLessonsByTeacherAndDay " + lesson.getTeacherName());
                }
            }
        }
        return filteredLessons;
    }
    public List<Lesson> getLessonsByTimePeriodAndTeacherAndDay(String timePeriod, String day, String teacherName) {
        List<Lesson> lessonsByDayAndTeacher = getFilteredLessonsByDayAndTeacher(day, teacherName);
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
    updateEverythingFromSQL();

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
        for(Group group: getGroupsFromSQL()){
            if(lesson.getGroupId() == group.getId()){
                lesson.setGroupName(group.getGroupName());
                break;
            }
        }
        lesson.setSubjectName("unknown");
        for(Subject subject: getSubjectsFromSQL()){
            if(lesson.getSubjectId() == subject.getId()){
                lesson.setSubjectName(subject.getSubjectName());
                break;
            }
        }
        lesson.setCabinetName("unknown");
        for(Cabinet cabinet: getCabinetsFromSQL()){
            if(lesson.getCabinetId() == cabinet.getId()){
                lesson.setCabinetName(cabinet.getCabinetName());
                break;
            }
        }
        lesson.setTeacherName("unknown");
        for(Teacher teacher: getTeachersFromSQL()){
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

    private void updateTeachersFromSQL(){
        databaseProcessing database = new databaseProcessing();
        setTeachersFromSQL(database.getTeacherListFromSQL());
        database = null;
    }
    private void updateCabinetsFromSQL(){
        databaseProcessing database = new databaseProcessing();
        setCabinetsFromSQL(database.getCabinetListFromSQL());
        database = null;
    }
    private void updateSubjectsFromSQL(){
        databaseProcessing database = new databaseProcessing();
        setSubjectsFromSQL(database.getSubjectListFromSQL());
        database = null;
    }
    private void updateGroupsFromSQL(){
        databaseProcessing database = new databaseProcessing();
        setGroupsFromSQL(database.getGroupListFromSQL());
        database = null;
    }
    private void updateLessonsFromSQL(){
        databaseProcessing database = new databaseProcessing();
        setLessonsFromSQL(database.getLessonsListFromSQL());
        database = null;
    }

    public void updateEverythingFromSQL(){
        updateTeachersFromSQL();
        updateGroupsFromSQL();
        updateCabinetsFromSQL();
        updateSubjectsFromSQL();
        updateLessonsFromSQL();
    }

}
