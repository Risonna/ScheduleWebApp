package com.risonna.schedulewebapp.hibernate.entity;

import com.risonna.schedulewebapp.database.DataHelper;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "admins_teachers")
@Named
@ViewScoped
public class AdminTeacher implements Serializable {

    @Id
    @Column(name = "userid", nullable = false, length = 50)
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    private transient List<String> adminTeacherList;

    public AdminTeacher(){
        updateAdminsTeachers();
    }
    public void addTeacher() throws NoSuchAlgorithmException {
        System.out.println(userid);
        if(userid!=null){
            DataHelper.getInstance().insertAdminsTeachers(userid);
            boolean userExists = false;
            for (Users users:DataHelper.getInstance().getAllUsers()){
                if(users.getUserid().equals(userid)){
                    userExists = true;
                    break;
                }
            }
            if(userExists){
                setTheRole();
            }
            else{
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedPassword = md.digest("1111".getBytes());
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedPassword) {
                    sb.append(String.format("%02x", b));
                }
                String hashedPasswordStr = sb.toString();
                DataHelper.getInstance().insertUsers(userid, hashedPasswordStr, "", true);
                DataHelper.getInstance().insertUsersGroups(userid, "user");
                setTheRole();
            }
        }
        else System.out.println("userid is null");
        updateAdminsTeachers();
    }

    public List<String> getAdminTeacherList() {
        return adminTeacherList;
    }

    public void setAdminTeacherList(List<String> adminTeacherList) {
        this.adminTeacherList = adminTeacherList;
    }
    public void updateAdminsTeachers(){
        setAdminTeacherList(DataHelper.getInstance().getAllAdminsTeachers());
    }

    public void deleteTeacher(String teacher) {
        // Delete the teacher from the database using teacher login
        // ...
        DataHelper.getInstance().deleteAdminTeacher(teacher);
        removeTheRole(teacher);
        updateAdminsTeachers();

        // Update the teacher list
    }

    public void setTheRole(){

        DataHelper.getInstance().insertUsersGroups(userid, "admin_teacher");

    }

    public void removeTheRole(String teacher){
        DataHelper.getInstance().deleteUsersGroups("admin_teacher", teacher);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminTeacher that = (AdminTeacher) o;
        return Objects.equals(userid, that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid);
    }
}
