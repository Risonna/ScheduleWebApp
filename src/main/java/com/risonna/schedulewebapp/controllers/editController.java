package com.risonna.schedulewebapp.controllers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class editController implements Serializable {
    public editController(){

    }

    public String dataAutomatic(){
        return "dataAutomatic";
    }
    public String dataManual(){
        return "homePage";
    }

    public String parsing(){
        return "parsing";
    }

    public String testing(){
        return "testing";
    }
}
