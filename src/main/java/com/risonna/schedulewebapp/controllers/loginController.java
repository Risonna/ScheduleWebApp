package com.risonna.schedulewebapp.controllers;



import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class loginController {
    public loginController(){

    }

    public String login(){
        return "login";
    }

    public String register(){
        return "register";
    }
    public String edit(){
        return "edit";
    }
}
