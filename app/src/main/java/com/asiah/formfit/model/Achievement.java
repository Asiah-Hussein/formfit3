package com.asiah.formfit.model;

import java.util.Date;

public class Achievement {
    private long id;
    private long userId;
    private String name;
    private String description;
    private Date date;

    public Achievement() {
        // Default constructor
    }

    public Achievement(long userId, String name, String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.date = new Date(); // Set to current date by default
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
