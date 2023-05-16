package com.risonna.schedulewebapp.beans;
import com.risonna.schedulewebapp.database.ScheduleDatabase;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.apache.commons.codec.digest.DigestUtils;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Named
@RequestScoped
public class register implements Serializable {
    private String username;
    private String password;
    private String passwordConfirm;
    private String email;

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

    public void registerUser() throws SQLException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedPassword = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        String hashedPasswordStr = sb.toString();
        Connection conn = ScheduleDatabase.getConnection();
        PreparedStatement prepStmt = conn.prepareStatement("INSERT INTO users (userid, password, email) VALUES (?, ?, ?)");
        prepStmt.setString(1, username);
        prepStmt.setString(2, hashedPasswordStr);
        prepStmt.setString(3, email);
        prepStmt.executeUpdate();

        prepStmt = conn.prepareStatement("INSERT INTO users_groups (groupid, userid) VALUES (?, ?)");

        prepStmt.setString(1, "user");
        prepStmt.setString(2, username);
        prepStmt.executeUpdate();

    }
}
