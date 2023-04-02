package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;


@Named
@SessionScoped
public class User implements Serializable {
    private String username;
    private String password;

    public User(){

    }

    public void setUsername(String username){
        this.username = username;

    }
    public void setPassword(String password){
        this.password = password;
    }


    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }


}
