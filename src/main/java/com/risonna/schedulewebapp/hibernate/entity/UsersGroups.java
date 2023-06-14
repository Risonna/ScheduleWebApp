package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users_groups")
@IdClass(UsersGroupsPK.class)
@Named
public class UsersGroups implements Serializable {
    @Id
    @Column(name = "groupid", nullable = false, length = 20)
    private String groupid;

    @Id
    @Column(name = "userid", nullable = false, length = 50)
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
        UsersGroups that = (UsersGroups) o;
        return Objects.equals(groupid, that.groupid) && Objects.equals(userid, that.userid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupid, userid);
    }
}
