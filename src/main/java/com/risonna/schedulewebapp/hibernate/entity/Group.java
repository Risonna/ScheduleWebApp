package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "studentgroups")
@Named
public class Group {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String groupName;
    @Basic
    @Column(name = "fullname", nullable = true, length = 100)
    private String fullGroupName;
    @Basic
    @Column(name = "institute", nullable = true, length = 150)
    private String institute;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

    public String getFullGroupName() {
        return fullGroupName;
    }

    public void setFullGroupName(String fullname) {
        this.fullGroupName = fullname;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group that = (Group) o;
        return id == that.id && Objects.equals(groupName, that.groupName) && Objects.equals(fullGroupName, that.fullGroupName) && Objects.equals(institute, that.institute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, fullGroupName, institute);
    }
}
