package com.risonna.schedulewebapp.controllers;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ScheduleDepartmentController extends ScheduleController{

    public ScheduleDepartmentController(){
        this.setSelectedTeacher(this.getTeachersForDepartment().get(1));
        this.setSelectedDepartment(this.getDepartmentList().get(0));
        this.setSelectedCabinet(this.getCabinetList().get(0));
        this.setSelectedGroup(this.getGroupNames().get(0));
        this.setSelectedDayOfWeek(this.getDaysOfWeek().get(0));
    }

    @Override
    public String getSelectedDepartment(){
        return super.getSelectedDepartment();
    }
    @Override
    public void updateTeacherList(){
        super.updateTeacherList();
        this.setSelectedTeacher(this.getTeachersForDepartment().get(1));

    }

}
