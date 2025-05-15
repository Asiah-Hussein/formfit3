package com.asiah.formfit.data;

import java.util.Date;

/**
 * User model represents a user in the Form-Fit application
 */
public class User {
    private long id;
    private String username;
    private String email;
    private Date joinDate;

    public User() {
        // Default constructor
        this.joinDate = new Date();
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.joinDate = new Date(); // Set to current date by default
    }

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}