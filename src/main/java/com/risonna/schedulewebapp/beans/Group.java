package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@ApplicationScoped
public class Group implements Serializable {
    private int id;
    private String groupName;
    private String fullGroupName;
    private String institute;
    public Group(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getFullGroupName() {
        return fullGroupName;
    }

    public String getInstitute() {
        return institute;
    }

    public void setFullGroupName(String fullGroupName) {
        this.fullGroupName = fullGroupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
