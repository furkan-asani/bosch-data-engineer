package com.bosch.datasynchronization.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class SynchronizationRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lastrun")
    private Date lastRun = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastRun() {
        return lastRun;
    }

    public void setLastRun(Date lastRun) {
        this.lastRun = lastRun;
    }
}
