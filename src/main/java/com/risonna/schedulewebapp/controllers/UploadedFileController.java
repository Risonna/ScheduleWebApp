package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.database.DataHelper;
import com.risonna.schedulewebapp.excelparsing.ExcelSearch;
import com.risonna.schedulewebapp.hibernate.entity.Cabinet;
import com.risonna.schedulewebapp.hibernate.entity.Group;
import com.risonna.schedulewebapp.hibernate.entity.Lesson;
import com.risonna.schedulewebapp.hibernate.entity.Subject;
import com.risonna.schedulewebapp.hibernate.entity.Teacher;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.file.UploadedFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class UploadedFileController implements Serializable {

    private UploadedFile uploadedFile;
    private String filePath;

    public static ArrayList<Lesson> lessonsStatic;
    private ArrayList<Lesson> listOfLessonsForGroup;

    private Boolean isUploaded;

    private ExcelSearch excelSearch;


    public ArrayList<Lesson> getListOfLessonsForGroup() {
        return listOfLessonsForGroup;
    }

    public void setListOfLessonsForGroup(ArrayList<Lesson> listOfLessonsForGroup) {
        this.listOfLessonsForGroup = listOfLessonsForGroup;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
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
        List<Teacher> teachers = DataHelper.getInstance().getAllTeachers();
        List<Subject> subjects = DataHelper.getInstance().getAllSubjects();
        List<Cabinet> cabinets = DataHelper.getInstance().getAllCabinets();
        List<Group> groups = DataHelper.getInstance().getAllGroups();

        setExcelSearch(new ExcelSearch(filePath, teachers, subjects, cabinets, groups));


        excelSearch.parseStuff();
        

        DataHelper.getInstance().insertLessons(excelSearch.getLessonList());



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
