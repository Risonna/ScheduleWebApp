package com.risonna.schedulewebapp.controllers;



import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class LoginController {
    public LoginController(){

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
