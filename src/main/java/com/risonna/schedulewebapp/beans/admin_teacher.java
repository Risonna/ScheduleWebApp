package com.risonna.schedulewebapp.beans;

import com.risonna.schedulewebapp.database.databaseProcessing;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class admin_teacher implements Serializable {
    private String teacherLogin;
    private List<String> adminTeacherList;

    public admin_teacher(){
        updateAdminsTeachers();
    }

    public String getTeacherLogin() {
        return teacherLogin;
    }

    public void setTeacherLogin(String teacherLogin) {
        this.teacherLogin = teacherLogin;
    }

    public void addTeacher(){
        databaseProcessing database = new databaseProcessing();
        database.addAdminTeacher(teacherLogin);
        updateAdminsTeachers();
    }

    public List<String> getAdminTeacherList() {
        return adminTeacherList;
    }

    public void setAdminTeacherList(List<String> adminTeacherList) {
        this.adminTeacherList = adminTeacherList;
    }

    public void updateAdminsTeachers(){
        databaseProcessing database = new databaseProcessing();
        setAdminTeacherList(database.getAdminsTeachers());
    }

    public void deleteTeacher(String teacher) {
        // Delete the teacher from the database using teacher login
        // ...
        databaseProcessing database = new databaseProcessing();
        database.removeAdminsTeachers(teacher);
        updateAdminsTeachers();

        // Update the teacher list
    }
}
