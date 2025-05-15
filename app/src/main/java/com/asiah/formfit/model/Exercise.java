package com.asiah.formfit.model;

import java.util.Date;

public class Exercise {
    private long id;
    private long userId;
    private String name;
    private int duration; // in seconds
    private float formAccuracy; // percentage from 0-100
    private int reps;
    private int calories;
    private Date timestamp;
    private boolean synced;

    public Exercise() {
        // Default constructor
    }

    public Exercise(long userId, String name, int duration, float formAccuracy, int reps, int calories) {
        this.userId = userId;
        this.name = name;
        this.duration = duration;
        this.formAccuracy = formAccuracy;
        this.reps = reps;
        this.calories = calories;
        this.timestamp = new Date(); // Set to current date by default
        this.synced = false; // Not synced by default
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getFormAccuracy() {
        return formAccuracy;
    }

    public void setFormAccuracy(float formAccuracy) {
        this.formAccuracy = formAccuracy;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }
}