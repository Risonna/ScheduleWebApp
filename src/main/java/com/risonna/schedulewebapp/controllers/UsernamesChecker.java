package com.risonna.schedulewebapp.controllers;

import com.risonna.schedulewebapp.beans.User;
import com.risonna.schedulewebapp.database.DatabaseProcessing;

import java.util.ArrayList;
import java.util.List;

public class UsernamesChecker {
    private List<String> usernames;

    public List<String> getUsernames() {
        return usernames;
    }

    public UsernamesChecker(){
        usernames = new ArrayList<>();
        DatabaseProcessing database = new DatabaseProcessing();
        List<User> users = new ArrayList<>();
        users = database.getUsersFromSQL();
        for(User user: users){
            usernames.add(user.getUsername());
        }
        users = null;
        database = null;
    }
}
