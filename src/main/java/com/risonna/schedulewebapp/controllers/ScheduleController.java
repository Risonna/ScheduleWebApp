package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.hibernate.entity.Teacher;
import com.risonna.schedulewebapp.hibernate.entity.Subject;
import com.risonna.schedulewebapp.hibernate.entity.Cabinet;
import com.risonna.schedulewebapp.hibernate.entity.Group;
import com.risonna.schedulewebapp.hibernate.entity.Lesson;
import com.risonna.schedulewebapp.database.DataHelper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

import java.util.*;



@Named
@ViewScoped
public class ScheduleController implements Serializable {
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

    private List<String> cabinetsString = Arrays.asList("1бл", "2бл", "2130а", "2130б", "2130в", "2131в", "2134", "2139",
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

    private List<Cabinet> cabinets = new ArrayList<>();
    public void setCabinets(List<Cabinet> cabinets) {
        this.cabinets = cabinets;
    }

    public List<Cabinet> getCabinets() {
        for (String cabinet:cabinetsString){
            Cabinet cabinet1 = new Cabinet();
            cabinet1.setType("unknown");
            cabinet1.setSeats("unknown");
            cabinet1.setCabinetName(cabinet);
            this.cabinets.add(cabinet1);
        }
        return cabinets;
    }

    private boolean isCabinetChecked = false;
    private final String[] TIME_PERIODS = {"8.00-9.35", "9.45-11.20", "11.45-13.20", "13.30-15.05", "15.30-17.05", "17.15-18.50", "19.00-20.35"};
    private final int TIME_PERIODS_length = TIME_PERIODS.length;

    public int getTIME_PERIODS_length() {
        return TIME_PERIODS_length;
    }

    public String[] getTIME_PERIODS() {
        return TIME_PERIODS;
    }

    private String selectedGroup;
    private String selectedTeacher;
    private String selectedDayOfWeek;
    private String selectedDepartment;
    private String selectedCabinet;
    private List<String> daysOfWeek;
    private List<String> cabinetList = new ArrayList<>();
    private List<String> groupNames;
    private List<String> departmentList;
    private List<Lesson> filteredLessonsByDayAndGroup;
    private List<Lesson> lessonsByTimePeriodAndGroupAndDay;
    private List<Lesson> lessonsByTimePeriodAndTeacherAndDay;
    private List<Lesson> filteredLessonsByDayAndCabinet;
    private List<Lesson> lessonsByTimePeriodAndCabinetAndDay;
    private List<List<Lesson>> filteredAndSortedLessonsForGroups;


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

    public List<String> getCabinetList() {
        List<String> listOfCabinets = new ArrayList<>();
        for (Cabinet cabinet:getCabinetsFromSQL()) {
            if(cabinet.getCabinetName() != null && !cabinet.getCabinetName().replaceAll("\\s", "").equalsIgnoreCase("unknown")){
                listOfCabinets.add(cabinet.getCabinetName());
            }
        }
        return listOfCabinets;
    }

    public String getSelectedCabinet() {
        return selectedCabinet;
    }

    public void setSelectedCabinet(String selectedCabinet) {
        this.selectedCabinet = selectedCabinet;
    }

    public void setCabinetList(List<String> cabinetList) {
        this.cabinetList = cabinetList;
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
            if(teacher.getTeacherName().replaceAll("\\s", "").equalsIgnoreCase("unknown"))continue;
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
            if(teacher.getTeacherName().replaceAll("\\s", "").equalsIgnoreCase("unknown"))continue;
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
            if(!group.getGroupName().replaceAll("\\s", "").equalsIgnoreCase("unknown")) {
                groupNames.add(group.getGroupName());
            }
        }
        return groupNames;
    }

    //get which lessons to show for schedule
    public List<Lesson> getFilteredLessonsByDayAndTeacher(String day, String teacherName) {
        List<Lesson> filteredLessons = new ArrayList<>();
        if (teacherName != null) {
            for (Lesson lesson : getLessonsOk()) {
                if (lesson.getLessonDay().equalsIgnoreCase(day) && lesson.getTeacherName().equalsIgnoreCase(teacherName)) {
                    filteredLessons.add(lesson);
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
            }
        }

        return lessonsByTimePeriod;
    }

    public List<List<Lesson>> getFilteredAndSortedLessonsForGroups(List<Lesson> listHuh) {
        List<List<Lesson>> listOfListsOfLessons = new ArrayList<>();

        List<Integer> rowPositions = new ArrayList<>();
        List<Integer> colPositions = new ArrayList<>();
        int rowAmount = 0;
        int colAmount = 0;
        int currentMultipleLesson = 0;
        int colForCopiedCheck = -20;
        List<Lesson> copiedlistHuh = new ArrayList<>();
        for (Lesson lesson : listHuh) {
            Lesson copiedLesson = new Lesson(lesson);
            if(!copiedLesson.isMultipleLessonsInOneCell() || (copiedLesson.getColFirst() != colForCopiedCheck && colForCopiedCheck !=-20))currentMultipleLesson=0;
            if(copiedLesson.isMultipleLessonsInOneCell()) {
                colForCopiedCheck = lesson.getColFirst();
                currentMultipleLesson++;
                if (currentMultipleLesson>1){
                    int rowFirst = copiedLesson.getRowFirst();
                    copiedLesson.setRowFirst(rowFirst+currentMultipleLesson-1);
                    copiedLesson.setRowLast(copiedLesson.getRowLast()+currentMultipleLesson-1);

                }
            }
            if (!rowPositions.contains(copiedLesson.getRowFirst())) {
                rowPositions.add(copiedLesson.getRowFirst());
                rowAmount++;
            }
            if(copiedLesson.getGroupColLast()-copiedLesson.getGroupColFirst()>0)colAmount=copiedLesson.getGroupColLast()-copiedLesson.getGroupColFirst()+1;
//            if(!colPositions.contains(lesson.getColFirst()) && !colPositions.contains(lesson.getColLast())){
//                    colPositions.add(lesson.getColFirst());
//                    colAmount++;
//            }


//            if(lesson.isMultipleLessonsInOneCell()){

////                colPositions.add(lesson.getColLast()+1);
////                if(colAmount <2)colAmount++;
//                if(currentRow != 0 && (currentRow - lesson.getRowFirst() >= 1)) {
//                    lesson.setRowFirst(currentRow);
//                }
//                currentRow = lesson.getRowFirst()+1;
////                if(!rowPositions.contains(lesson.getRowFirst())){
////                    rowPositions.add(lesson.getRowFirst());
////                    rowAmount++;
////                }
//                if(!rowPositions.contains(lesson.getRowFirst())) {
//                    rowAmount++;
//                    rowPositions.add(lesson.getRowFirst());
//                }
//
//            }
            copiedlistHuh.add(copiedLesson);
        }

        for (int i = 0; i < rowAmount; i++) {
            List<Lesson> listOfLessons = new ArrayList<>();
            int currentrownum = rowPositions.get(i);
            int rowFirst = rowPositions.get(0);
            int rowLast = rowPositions.get(rowAmount-1);
            Collections.sort(copiedlistHuh, new Comparator<Lesson>() {
                @Override
                public int compare(Lesson lesson1, Lesson lesson2) {
                    return lesson1.getColFirst() - lesson2.getColFirst();
                }
            });
            for (Lesson lesson : copiedlistHuh) {
                int colFirst = lesson.getGroupColFirst();
                int colLast = lesson.getGroupColLast();
                if (lesson.getRowFirst() == currentrownum || lesson.getRowLast() == currentrownum) {

                    if(!lesson.isForWholeGroup() && !lesson.isMultipleLessonsInOneCell() &&
                            (lesson.getRowLast() != currentrownum || lesson.getRowFirst() != currentrownum))lesson.setRowSpan(rowAmount);
                    else lesson.setRowSpan(1);


                    if(lesson.isForWholeGroup())lesson.setColSpan(colAmount);
                    else lesson.setColSpan(1);

                    boolean isThereLessonAfter = true;

                    if(colAmount>1 && lesson.getColLast() == colLast && lesson.getColFirst() != colFirst && !lesson.isForWholeGroup()){
                        boolean isThereLessonBefore = false;
                        for (Lesson lesson1:copiedlistHuh) {
                            if ((lesson1.getRowFirst() == currentrownum || lesson1.getRowLast() == currentrownum)
                                    && lesson1.getColLast() != lesson.getColLast() && lesson1.getColFirst() != lesson.getColFirst()) {
                                isThereLessonBefore = true;
                                break;
                            }

                        }

                        if(!isThereLessonBefore && (lesson.getRowFirst() == currentrownum) && lesson.getColFirst() != lesson.getGroupColFirst()) {
                            Lesson emptyLesson = new Lesson();
                            emptyLesson.setColSpan(1);
                            emptyLesson.setRowSpan(1);
                            emptyLesson.setColFirst(lesson.getColFirst() + 1);
                            emptyLesson.setColLast(lesson.getColLast() + 1);
                            emptyLesson.setRowFirst(lesson.getRowFirst());
                            emptyLesson.setRowLast(lesson.getRowLast());
                            emptyLesson.setGroupColFirst(lesson.getGroupColFirst());
                            emptyLesson.setGroupColLast(lesson.getGroupColLast());
                            listOfLessons.add(emptyLesson);
                        }
                    }

                    if(colAmount>1 && lesson.getColFirst() == colFirst && lesson.getColLast() != colLast && !lesson.isForWholeGroup()){
                        isThereLessonAfter = false;
                        for (Lesson lesson1:copiedlistHuh) {
                            if ((lesson1.getRowFirst() == currentrownum || lesson1.getRowLast() == currentrownum)
                                    && lesson1.getColLast() != lesson.getColLast() && lesson1.getColFirst() != lesson.getColFirst()) {
                                isThereLessonAfter = true;
                                break;
                            }

                        }

                        if(!isThereLessonAfter && (lesson.getRowFirst() == currentrownum) && lesson.getColLast() != lesson.getGroupColLast()) {
                            Lesson emptyLesson = new Lesson();
                            emptyLesson.setColSpan(1);
                            emptyLesson.setRowSpan(1);
                            listOfLessons.add(lesson);
                            listOfLessons.add(emptyLesson);
                        }
                    }

                    if(isThereLessonAfter && !listOfLessons.contains(lesson) && lesson.getRowFirst() == currentrownum){listOfLessons.add(lesson);}

                }
//                rowFirst = lesson.getRowFirst();
//                rowLast = lesson.getRowLast();
//                colFirst = lesson.getColFirst();
//                colLast = lesson.getColLast();
            }
            listOfListsOfLessons.add(listOfLessons);
        }

        return listOfListsOfLessons;
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



    public List<Lesson> getFilteredLessonsByDayAndCabinet(String day, String cabinetName) {
        List<Lesson> filteredLessons = new ArrayList<>();
        if (cabinetName != null) {
            for (Lesson lesson : getLessonsOk()) {
                if (lesson.getLessonDay().replaceAll("\\s", "").equalsIgnoreCase(day.replaceAll("\\s",
                        "")) && lesson.getCabinetName().replaceAll("\\s", "").equalsIgnoreCase(cabinetName.replaceAll("\\s",
                        ""))) {
                    filteredLessons.add(lesson);
                }
                else{
                }
            }
        }
        else{
        }
        return filteredLessons;
    }
    public List<Lesson> getLessonsByTimePeriodAndCabinetAndDay(String timePeriod, String day, String cabinetName){
        List<Lesson> lessonsByDayAndTeacher = getFilteredLessonsByDayAndCabinet(day, cabinetName);
        List<Lesson> lessonsByTimePeriod = new ArrayList<>();
        for (Lesson lesson : lessonsByDayAndTeacher) {
            if (lesson.getLessonTime().replaceAll("\\s", "").contains(timePeriod.replaceAll("\\s", ""))) {
                boolean hasLessonWithDayTime = false;
                if(!lessonsByTimePeriod.isEmpty()) {
                    for (Lesson lessonByTime : lessonsByTimePeriod) {
                        if (lessonByTime.getLessonTime().replaceAll("\\s",
                                "").equalsIgnoreCase(lesson.getLessonTime().replaceAll("\\s", "")) &&
                                lessonByTime.getLessonDay().replaceAll("\\s", "").equalsIgnoreCase(lesson.getLessonDay().replaceAll(
                                        "\\s", "")) &&
                                lessonByTime.getLessonWeek().equalsIgnoreCase(lesson.getLessonWeek()) &&
                                lessonByTime.getTeacherName().replaceAll("\\s", "").equalsIgnoreCase(lesson.getTeacherName().replaceAll(
                                        "\\s", ""))) {
                            hasLessonWithDayTime = true;
                            break;
                        }
                    }
                }
                if(!hasLessonWithDayTime){
                    lessonsByTimePeriod.add(lesson);
                }
            }
        }

        return lessonsByTimePeriod;
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

    public ScheduleController() {
        updateEverythingFromSQL();
        if(getTeacherNameList().size() > 1 && getDepartmentList().size() >1 && getGroupNames().size() >1 &&
                getCabinetList().size() > 1 && getDaysOfWeek().size() >1){
            this.selectedTeacher = getTeacherNameList().get(0);
            this.selectedCabinet = getCabinetList().get(0);
            this.selectedGroup = getGroupNames().get(0);
            this.selectedDepartment = getDepartmentList().get(0);
            this.selectedDayOfWeek = getDaysOfWeek().get(0);
        }

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

    public List<String> checkCabinetUsageNoFacesContext(List<Lesson> allLessons) {
        String cabinet = "Кабинет";
        String inuse = "используется";
        String byteachers = "преподавателями";
        String usedand = "и";
        List<String> repeatedCabinetUse = new ArrayList<>();
        Set<String> seenCabinetUses = new HashSet<>();

        for (int i = 0; i < allLessons.size(); i++) {
            Lesson lesson = allLessons.get(i);
            String cabinetToCheck = lesson.getCabinetName();
            String cabinetTime = lesson.getLessonTime();
            String cabinetDay = lesson.getLessonDay();
            String teacherToCheck = lesson.getTeacherName();

            for (int j = i + 1; j < allLessons.size(); j++) {
                Lesson lessonj = allLessons.get(j);
                if (lesson.getCabinetName().replaceAll("\\s", "").equalsIgnoreCase("unknown")) continue;

                if (lesson.getLessonWeek().replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonWeek().replaceAll("\\s", "")) &&
                        cabinetToCheck.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getCabinetName().replaceAll("\\s", "")) &&
                        cabinetTime.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonTime().replaceAll("\\s", "")) &&
                        cabinetDay.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonDay().replaceAll("\\s", "")) &&
                        !teacherToCheck.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getTeacherName().replaceAll("\\s", ""))) {

                    String cabinetUse = cabinet + " " + cabinetToCheck + " " + inuse + " " +
                            cabinetTime + " " + cabinetDay + " " + byteachers + " " + lessonj.getTeacherName() + " " +
                            usedand + " " + teacherToCheck;

                    if (!seenCabinetUses.contains(cabinetUse)) {
                        repeatedCabinetUse.add(cabinetUse);
                        seenCabinetUses.add(cabinetUse);
                        System.out.println(cabinetUse);
                    }
                }
            }
        }

        setCabinetChecked(true);
        return repeatedCabinetUse;
        // Do something with the repeatedCabinetUse list, or return it based on your needs
    }

    public List<String> checkCabinetUsage(List<Lesson> allLessons) {
        String baseName = "nls.messages"; // Without the "resources" folder and file extension
        ResourceBundle bundle = ResourceBundle.getBundle(baseName, FacesContext.getCurrentInstance().getViewRoot().getLocale());
        String cabinet = bundle.getString("cabinet");
        String inuse = bundle.getString("inuse");
        String byteachers = bundle.getString("usedbyteachers");
        String usedand = bundle.getString("and");
        List<String> repeatedCabinetUse = new ArrayList<>();
        Set<String> seenCabinetUses = new HashSet<>(); // Keep track of identified cabinet usage combinations

        for (int i = 0; i < allLessons.size(); i++) {
            Lesson lesson = allLessons.get(i);
            String cabinetToCheck = lesson.getCabinetName();
            String cabinetTime = lesson.getLessonTime();
            String cabinetDay = lesson.getLessonDay();
            String teacherToCheck = lesson.getTeacherName(); // Get the teacher name

            for (int j = i + 1; j < allLessons.size(); j++) { // Only check the remaining lessons in the list
                Lesson lessonj = allLessons.get(j);
                if(lesson.getCabinetName().replaceAll("\\s", "").equalsIgnoreCase("unknown"))continue;
                if (lesson.getLessonWeek().replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonWeek().replaceAll("\\s", "")) &&
                        cabinetToCheck.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getCabinetName().replaceAll("\\s", "")) &&
                        cabinetTime.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonTime().replaceAll("\\s", "")) &&
                        cabinetDay.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getLessonDay().replaceAll("\\s", "")) &&
                        !teacherToCheck.replaceAll("\\s", "").equalsIgnoreCase(lessonj.getTeacherName().replaceAll("\\s", ""))) { // Check if different teachers are using the same cabinet
                    String cabinetUse = cabinet + " " + cabinetToCheck + " " + inuse + " " +
                            cabinetTime + " " + cabinetDay + " " + byteachers + " " + lessonj.getTeacherName() + " " +
                            usedand + " " + teacherToCheck;
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

    public boolean isTeacherLessonsMoreThanOne(String teacherName){

        for(Lesson lesson:lessonsOk){
            if(lesson.getTeacherName().equals(teacherName))return true;
        }

        return false;
    }

    public void updateTeacherList(){
        getTeachersForDepartment();
    }
    private void updateTeachersFromSQL(){
        setTeachersFromSQL(DataHelper.getInstance().getAllTeachers());
    }
    private void updateCabinetsFromSQL(){
        setCabinetsFromSQL(DataHelper.getInstance().getAllCabinets());
    }
    private void updateSubjectsFromSQL(){
        setSubjectsFromSQL(DataHelper.getInstance().getAllSubjects());
    }
    private void updateGroupsFromSQL(){
        setGroupsFromSQL(DataHelper.getInstance().getAllGroups());
    }
    private void updateLessonsFromSQL(){
        setLessonsFromSQL(DataHelper.getInstance().getAllLessons());
    }

    public void updateEverythingFromSQL(){
        updateTeachersFromSQL();
        updateGroupsFromSQL();
        updateCabinetsFromSQL();
        updateSubjectsFromSQL();
        updateLessonsFromSQL();
    }



}