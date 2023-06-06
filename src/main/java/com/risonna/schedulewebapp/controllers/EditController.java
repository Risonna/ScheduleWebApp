package com.risonna.schedulewebapp.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class EditController implements Serializable {
    public EditController(){

    }

    public String dataAutomatic(){
        return "dataAutomatic.xhtml?faces-redirect=true";
    }
    public String dataManual(){
        return "homePage";
    }

    public String parsing(){
        return "parsing.xhtml?faces-redirect=true";
    }

    public String testing(){
        return "testing.xhtml?faces-redirect=true";
    }

    public String addTeachersAdmins(){
        return "addTeacherAdmin.xhtml?faces-redirect=true";
    }
}
