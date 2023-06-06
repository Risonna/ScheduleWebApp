package com.risonna.schedulewebapp.beans;
import com.risonna.schedulewebapp.database.DatabaseProcessing;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Named
@RequestScoped
public class Register implements Serializable {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;
    private boolean registered_via_kemsu;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public String getPasswordConfirm(){
        return passwordConfirm;
    }
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }


    public String getUsername() {
        return username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String registerUser() throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        String hashedPasswordStr = sb.toString();
        DatabaseProcessing database = new DatabaseProcessing();

        database.addUser(username, hashedPasswordStr, email, registered_via_kemsu);

        database.addUsersGroups(username, "user");


        return "login";

    }

    public boolean isRegistered_via_kemsu() {
        return registered_via_kemsu;
    }

    public void setRegistered_via_kemsu(boolean registered_via_kemsu) {
        this.registered_via_kemsu = registered_via_kemsu;
    }
}
