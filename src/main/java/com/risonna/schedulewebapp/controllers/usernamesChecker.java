package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.User;
import com.risonna.schedulewebapp.database.databaseProcessing;

import java.util.ArrayList;
import java.util.List;

public class usernamesChecker {
    private List<String> usernames;

    public List<String> getUsernames() {
        return usernames;
    }

    public usernamesChecker(){
        usernames = new ArrayList<>();
        databaseProcessing database = new databaseProcessing();
        List<User> users = new ArrayList<>();
        users = database.getUsersFromSQL();
        for(User user: users){
            usernames.add(user.getUsername());
        }
        users = null;
        database = null;
    }
}
