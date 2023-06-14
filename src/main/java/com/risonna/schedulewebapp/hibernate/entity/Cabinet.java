package com.risonna.schedulewebapp.hibernate.entity;

import jakarta.inject.Named;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Named
@Table(name = "cabinets")
public class Cabinet {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "cabinetname", nullable = true, length = 45)
    private String cabinetName;
    @Basic
    @Column(name = "type", nullable = true, length = 45)
    private String type;
    @Basic
    @Column(name = "seats", nullable = true, length = 45)
    private String seats;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetname) {
        this.cabinetName = cabinetname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cabinet cabinets = (Cabinet) o;
        return id == cabinets.id && Objects.equals(cabinetName, cabinets.cabinetName) && Objects.equals(type, cabinets.type) && Objects.equals(seats, cabinets.seats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cabinetName, type, seats);
    }
}
