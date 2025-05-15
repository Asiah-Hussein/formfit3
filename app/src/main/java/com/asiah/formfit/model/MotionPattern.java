// File: app/src/main/java/com/asiah/formfit/model/MotionPattern.java
package com.asiah.formfit.model;

import java.util.Arrays;

/**
 * MotionPattern represents a pattern of movement detected by wearable sensors
 * Used for analyzing and comparing motion data during exercise
 */
public class MotionPattern {

    // Motion pattern types
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_CORRECT = 1;
    public static final int TYPE_INCORRECT = 2;

    // Error types
    public static final int ERROR_NONE = 0;
    public static final int ERROR_TOO_FAST = 1;
    public static final int ERROR_TOO_SLOW = 2;
    public static final int ERROR_WRONG_ANGLE = 3;
    public static final int ERROR_INCOMPLETE_RANGE = 4;

    private int type;
    private int errorType;
    private float[] accelerationData;
    private float[] gyroscopeData;
    private long timestamp;
    private float confidence;

    /**
     * Default constructor
     */
    public MotionPattern() {
        this.type = TYPE_UNKNOWN;
        this.errorType = ERROR_NONE;
        this.accelerationData = new float[3]; // x, y, z
        this.gyroscopeData = new float[3]; // x, y, z
        this.timestamp = System.currentTimeMillis();
        this.confidence = 0.0f;
    }

    /**
     * Constructor with sensor data
     */
    public MotionPattern(float[] accelerationData, float[] gyroscopeData) {
        this.type = TYPE_UNKNOWN;
        this.errorType = ERROR_NONE;
        this.accelerationData = accelerationData;
        this.gyroscopeData = gyroscopeData;
        this.timestamp = System.currentTimeMillis();
        this.confidence = 0.0f;
    }

    /**
     * Check if this pattern matches the expected pattern for the exercise
     */
    public boolean matchesExpectedPattern() {
        return type == TYPE_CORRECT;
    }

    /**
     * Calculate similarity score with another motion pattern
     * Higher score means more similar patterns
     */
    public float calculateSimilarity(MotionPattern other) {
        // Simple Euclidean distance calculation
        float accDistance = calculateEuclideanDistance(this.accelerationData, other.accelerationData);
        float gyroDistance = calculateEuclideanDistance(this.gyroscopeData, other.gyroscopeData);

        // Convert distance to similarity (inverse relationship)
        // Max distance is normalized to 1.0
        float maxDistance = (float) Math.sqrt(6 * Math.pow(20, 2)); // Assuming max sensor value is ±20 in each axis
        float normalizedDistance = (accDistance + gyroDistance) / maxDistance;

        // Convert to similarity score (0 to 1)
        return Math.max(0, 1 - normalizedDistance);
    }

    /**
     * Calculate Euclidean distance between two vectors
     */
    private float calculateEuclideanDistance(float[] v1, float[] v2) {
        float sum = 0;
        for (int i = 0; i < Math.min(v1.length, v2.length); i++) {
            sum += Math.pow(v1[i] - v2[i], 2);
        }
        return (float) Math.sqrt(sum);
    }

    // Getters and setters

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrorType() {
        return errorType;
    }

    public void setErrorType(int errorType) {
        this.errorType = errorType;
    }

    public float[] getAccelerationData() {
        return accelerationData;
    }

    public void setAccelerationData(float[] accelerationData) {
        this.accelerationData = accelerationData;
    }

    public float[] getGyroscopeData() {
        return gyroscopeData;
    }

    public void setGyroscopeData(float[] gyroscopeData) {
        this.gyroscopeData = gyroscopeData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "MotionPattern{" +
                "type=" + type +
                ", errorType=" + errorType +
                ", accelerationData=" + Arrays.toString(accelerationData) +
                ", gyroscopeData=" + Arrays.toString(gyroscopeData) +
                ", timestamp=" + timestamp +
                ", confidence=" + confidence +
                '}';
    }
}