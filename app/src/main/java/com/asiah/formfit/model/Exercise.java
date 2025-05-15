// File: app/src/main/java/com/asiah/formfit/data/Exercise.java
package com.asiah.formfit.data;

import java.util.Date;

public class Exercise {
    // Add these missing constants
    public static final int DIFFICULTY_BEGINNER = 1;
    public static final int DIFFICULTY_INTERMEDIATE = 2;
    public static final int DIFFICULTY_ADVANCED = 3;

    private long id;
    private long userId;
    private String name;
    private int duration; // in seconds
    private float formAccuracy; // percentage from 0-100
    private int reps;
    private int calories;
    private Date timestamp;
    private boolean synced;
    // Add these missing fields
    private int difficulty;
    private int iconResourceId;
    private String category;

    public Exercise() {
        // Default constructor
        this.timestamp = new Date();
        this.synced = false;
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

    // Constructor for exercise library items
    public Exercise(String name, String category, String difficulty, int iconResourceId) {
        this.name = name;
        this.category = category;
        this.setDifficultyFromString(difficulty);
        this.iconResourceId = iconResourceId;
        this.timestamp = new Date();
    }

    // Helper method to set difficulty from string
    private void setDifficultyFromString(String difficulty) {
        if ("Beginner".equals(difficulty)) {
            this.difficulty = DIFFICULTY_BEGINNER;
        } else if ("Intermediate".equals(difficulty)) {
            this.difficulty = DIFFICULTY_INTERMEDIATE;
        } else if ("Advanced".equals(difficulty)) {
            this.difficulty = DIFFICULTY_ADVANCED;
        } else {
            this.difficulty = DIFFICULTY_BEGINNER; // Default
        }
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

    // Add these getters and setters for the new fields
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public void setIconResourceId(int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}