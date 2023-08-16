package com.risonna.schedulewebapp.api.rs;

import com.risonna.schedulewebapp.beans.User;
import com.risonna.schedulewebapp.database.DataHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterUserRequest extends User {
    @Override
    public String registerUser() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(super.getPassword().getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        String hashedPasswordStr = sb.toString();

        DataHelper.getInstance().insertUsers(super.getUsername(), hashedPasswordStr, super.getEmail(), false);

        DataHelper.getInstance().insertUsersGroups(super.getUsername(), "user");
        hashedPasswordStr = null;

        return "homePage";

    }
}
