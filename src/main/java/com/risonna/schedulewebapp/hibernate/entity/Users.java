package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Users {

    @Id
    @Column(name = "userid", nullable = false, length = 50)
    private String userid;
    @Basic
    @Column(name = "password", nullable = false, length = 256)
    private String password;
    @Basic
    @Column(name = "email", nullable = true, length = 250)
    private String email;
    @Basic
    @Column(name = "creation_time", nullable = true)
    private Timestamp creationTime;
    @Basic
    @Column(name = "registered_via_kemsu", nullable = false)
    private boolean registeredViaKemsu;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public boolean getRegisteredViaKemsu() {
        return registeredViaKemsu;
    }

    public void setRegisteredViaKemsu(boolean registeredViaKemsu) {
        this.registeredViaKemsu = registeredViaKemsu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return registeredViaKemsu == users.registeredViaKemsu && Objects.equals(userid, users.userid) && Objects.equals(password, users.password) && Objects.equals(email, users.email) && Objects.equals(creationTime, users.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, password, email, creationTime, registeredViaKemsu);
    }
}
