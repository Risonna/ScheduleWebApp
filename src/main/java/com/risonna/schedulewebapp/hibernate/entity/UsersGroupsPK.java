package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

public class UsersGroupsPK implements Serializable {
    @Column(name = "groupid", nullable = false, length = 20)
    @Id
    private String groupid;
    @Column(name = "userid", nullable = false, length = 50)
    @Id
    private String userid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersGroupsPK that = (UsersGroupsPK) o;
        return Objects.equals(groupid, that.groupid) && Objects.equals(userid, that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupid, userid);
    }
}
