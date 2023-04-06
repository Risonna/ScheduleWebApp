package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.*;
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


    public void parse(String filePath) throws IOException {
        scheduleController getLessonInfoFromSQL = new scheduleController();
        List<Teacher> teachers = getLessonInfoFromSQL.getTeacherList();
        List<Subject> subjects = getLessonInfoFromSQL.getSubjectList();
        List<Cabinet> cabinets = getLessonInfoFromSQL.getCabinetList();
        List<Group> groups = getLessonInfoFromSQL.getGroupList();

        setExcelSearch(new ExcelSearch(filePath, teachers, subjects, cabinets, groups));
        excelSearch.parseStuff();
        listOfLessons = excelSearch.getLessonList();
        for (Lesson sex: listOfLessons) {

            System.out.println("the teacher is " + sex.getTeacherName());
            System.out.println("the teacher's id is " + sex.getTeacherId());
            System.out.println("the subject is " + sex.getSubjectName());
            System.out.println("the time is " + sex.getLessonTime());
            System.out.println("the day is  " + sex.getLessonDay());
            System.out.println("the cabinet is " + sex.getCabinetName());
            System.out.println("the cabinet's id is " + sex.getCabinetId());
            System.out.println("the group is " + sex.getGroupName());
            System.out.println("the group's id is " + sex.getGroupId());

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
