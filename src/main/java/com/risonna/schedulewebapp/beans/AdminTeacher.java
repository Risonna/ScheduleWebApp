package com.risonna.schedulewebapp.beans;

import com.risonna.schedulewebapp.database.DatabaseProcessing;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminTeacher implements Serializable {
    private String teacherLogin;
    private List<String> adminTeacherList;

    public AdminTeacher(){
        updateAdminsTeachers();
    }

    public String getTeacherLogin() {
        return teacherLogin;
    }

    public void setTeacherLogin(String teacherLogin) {
        this.teacherLogin = teacherLogin;
    }

    public void addTeacher(){
        DatabaseProcessing database = new DatabaseProcessing();
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
        DatabaseProcessing database = new DatabaseProcessing();
        setAdminTeacherList(database.getAdminsTeachers());
    }

    public void deleteTeacher(String teacher) {
        // Delete the teacher from the database using teacher login
        // ...
        DatabaseProcessing database = new DatabaseProcessing();
        database.removeAdminsTeachers(teacher);
        updateAdminsTeachers();

        // Update the teacher list
    }
}
