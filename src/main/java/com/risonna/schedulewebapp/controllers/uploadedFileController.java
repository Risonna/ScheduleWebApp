package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.Lesson;
import com.risonna.schedulewebapp.excelparsing.ExcelSearch;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@SessionScoped
public class uploadedFileController implements Serializable {

    private UploadedFile uploadedFile;
    private String filePath;

    private ArrayList<Lesson> listOfLessons;

    private Boolean isUploaded;

    private ExcelSearch excelSearch;

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public ArrayList<Lesson> getListOfLessons(){
        return listOfLessons;
    }
    public Boolean getIsUploaded(){
        return isUploaded;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void setExcelSearch(ExcelSearch excelsearch){
        this.excelSearch = excelsearch;
    }
    public String getFilePath(){
        return this.filePath;
    }

    public ArrayList<Lesson> dayTimeGroup(ArrayList<Lesson> allLessonList, String day, String group){
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

    public void parse(String filePath) throws IOException {
        List<String> subjects = Arrays.asList("Дифференциальные уравнения", "Дискретная математика", "Программирование", "Физика", "Математический анализ", "Базы данных", "Алгебра", "Введение в специальность", "Иностранный язык", "Циклические виды спорта", "Информатика", "История");
        List<String> teacherNames =  Arrays.asList("доц. Карабцев С.Н.", "доц. Завозкин С.Ю.", "доц. Фомина Л.Н.", "доц. Бурмин Л.Н.", "доц. Гордиенок Н.И.", "доц. Савельева И.В.", "асс. Илькевич В.В.", "проф. Медведев А.В.", "ст. пр. Тюкалова С.А.", "доц. Гринвальд О.Н.", "доц. Карпинец А.Ю.");

        setExcelSearch(new ExcelSearch(filePath, teacherNames, subjects));
        excelSearch.parseStuff();
        listOfLessons = excelSearch.getLessonList();
        for (Lesson sex: listOfLessons) {

            System.out.println("the teacher is " + sex.getTeacherName());
            System.out.println("the subject is " + sex.getSubjectName());
            System.out.println("the time is " + sex.getLessonTime());
            System.out.println("the day is  " + sex.getLessonDay());
            System.out.println("the cabinet is " + sex.getCabinetName());
            System.out.println("the group is " + sex.getGroupName());
            System.out.println("the group's full name is " + sex.getGroupNameFull());
            System.out.println("the institute is " + sex.getInstituteName());

        }
        System.out.println(listOfLessons);
        isUploaded = true;

    }
    public void upload() {
        try (InputStream input = uploadedFile.getInputStream()) {
            File file = new File(uploadedFile.getFileName());
            FileOutputStream output = new FileOutputStream(file);
            IOUtils.copy(input, output);
            filePath = file.getPath();
            // Read the uploaded file data here
            // You can save the file to a server directory or process it in memory
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
        }
    }

}
