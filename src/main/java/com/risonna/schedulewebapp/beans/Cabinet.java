package com.risonna.schedulewebapp.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ApplicationScoped
public class Cabinet implements Serializable {

    private int id;
    private String cabinetName;
    private String seats;
    private String type;
    public Cabinet(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public int getId() {
        return id;
    }

    public String getSeats() {
        return seats;
    }

    public String getType() {
        return type;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public void setType(String type) {
        this.type = type;
    }
}
