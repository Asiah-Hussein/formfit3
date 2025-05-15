// File: app/src/main/java/com/asiah/formfit/utils/MotionAnalyzer.java
package com.asiah.formfit.utils;

import android.util.Log;

import com.asiah.formfit.model.MotionPattern;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MotionAnalyzer analyzes motion data from wearable sensors
 * to detect exercise patterns and provide feedback
 */
public class MotionAnalyzer {

    private static final String TAG = "MotionAnalyzer";

    // Exercise types
    public static final int EXERCISE_SQUAT = 0;
    public static final int EXERCISE_PUSHUP = 1;
    public static final int EXERCISE_PLANK = 2;
    public static final int EXERCISE_LUNGE = 3;

    // Reference motion patterns for each exercise
    private Map<Integer, List<MotionPattern>> referencePatterns;

    // Similarity threshold for pattern matching
    private static final float SIMILARITY_THRESHOLD = 0.7f;

    /**
     * Constructor initializes reference motion patterns
     */
    public MotionAnalyzer() {
        // Initialize reference patterns
        initializeReferencePatterns();

        Log.d(TAG, "MotionAnalyzer initialized successfully");
    }

    /**
     * Initialize reference motion patterns for various exercises
     */
    private void initializeReferencePatterns() {
        referencePatterns = new HashMap<>();

        // For each exercise type, add reference patterns
        // In a real implementation, these would be trained/calibrated patterns

        // Squat reference patterns
        List<MotionPattern> squatPatterns = new ArrayList<>();

        // Pattern for starting position
        MotionPattern squatStart = new MotionPattern(
                new float[]{0, 0, 9.8f},  // Acceleration (standing)
                new float[]{0, 0, 0}       // Gyroscope (no rotation)
        );
        squatStart.setType(MotionPattern.TYPE_CORRECT);
        squatPatterns.add(squatStart);

        // Pattern for mid-squat
        MotionPattern squatMid = new MotionPattern(
                new float[]{0, -2.0f, 9.5f},  // Acceleration (moving down)
                new float[]{0.5f, 0, 0}       // Gyroscope (slight rotation)
        );
        squatMid.setType(MotionPattern.TYPE_CORRECT);
        squatPatterns.add(squatMid);

        // Pattern for bottom position
        MotionPattern squatBottom = new MotionPattern(
                new float[]{0, 0, 9.8f},      // Acceleration (momentary pause)
                new float[]{0, 0, 0}          // Gyroscope (no rotation)
        );
        squatBottom.setType(MotionPattern.TYPE_CORRECT);
        squatPatterns.add(squatBottom);

        // Add to reference map
        referencePatterns.put(EXERCISE_SQUAT, squatPatterns);

        // Add patterns for other exercise types as needed
        // This would be expanded in a real implementation
    }

    /**
     * Detect motion pattern from sensor data
     * @param accelerationData Acceleration data from wearable
     * @param gyroscopeData Gyroscope data from wearable
     * @param exerciseType Type of exercise being performed
     * @return Detected motion pattern
     */
    public MotionPattern detectPattern(float[] accelerationData, float[] gyroscopeData, int exerciseType) {
        // Create a new pattern from the sensor data
        MotionPattern currentPattern = new MotionPattern(accelerationData, gyroscopeData);

        // Get reference patterns for the exercise type
        List<MotionPattern> patterns = referencePatterns.get(exerciseType);
        if (patterns == null || patterns.isEmpty()) {
            Log.w(TAG, "No reference patterns found for exercise type: " + exerciseType);
            return currentPattern; // Return unclassified pattern
        }

        // Find the most similar reference pattern
        MotionPattern bestMatch = findBestMatch(currentPattern, patterns);

        // If similarity is above threshold, classify as the same type
        float similarity = currentPattern.calculateSimilarity(bestMatch);
        if (similarity >= SIMILARITY_THRESHOLD) {
            currentPattern.setType(bestMatch.getType());
        } else {
            // If not similar enough, classify as incorrect
            currentPattern.setType(MotionPattern.TYPE_INCORRECT);

            // Determine error type
            determineErrorType(currentPattern, bestMatch, exerciseType);
        }

        // Set confidence based on similarity
        currentPattern.setConfidence(similarity);

        Log.d(TAG, "Pattern detected with confidence: " + similarity);
        return currentPattern;
    }

    /**
     * Find the reference pattern that best matches the current pattern
     * @param currentPattern Pattern to match
     * @param referencePatterns List of reference patterns
     * @return Best matching reference pattern
     */
    private MotionPattern findBestMatch(MotionPattern currentPattern, List<MotionPattern> referencePatterns) {
        MotionPattern bestMatch = null;
        float bestSimilarity = 0;

        // Compare with each reference pattern
        for (MotionPattern refPattern : referencePatterns) {
            float similarity = currentPattern.calculateSimilarity(refPattern);

            // Update best match if this is better
            if (similarity > bestSimilarity) {
                bestSimilarity = similarity;
                bestMatch = refPattern;
            }
        }

        // If no match found, return the first reference pattern
        if (bestMatch == null && !referencePatterns.isEmpty()) {
            bestMatch = referencePatterns.get(0);
        }

        return bestMatch;
    }

    /**
     * Determine what type of error is occurring in the motion
     * @param currentPattern Current detected pattern
     * @param referencePattern Best matching reference pattern
     * @param exerciseType Type of exercise being performed
     */
    private void determineErrorType(MotionPattern currentPattern, MotionPattern referencePattern, int exerciseType) {
        // Get acceleration and gyroscope data
        float[] currAcc = currentPattern.getAccelerationData();
        float[] refAcc = referencePattern.getAccelerationData();
        float[] currGyro = currentPattern.getGyroscopeData();
        float[] refGyro = referencePattern.getGyroscopeData();

        // Check for common errors based on exercise type
        switch (exerciseType) {
            case EXERCISE_SQUAT:
                // Check for speed issues
                float verticalAccDiff = Math.abs(currAcc[1] - refAcc[1]);
                if (verticalAccDiff > 3.0f) {
                    if (currAcc[1] < refAcc[1]) {
                        currentPattern.setErrorType(MotionPattern.ERROR_TOO_FAST);
                    } else {
                        currentPattern.setErrorType(MotionPattern.ERROR_TOO_SLOW);
                    }
                }

                // Check for incorrect angle
                float rotationDiff = Math.abs(currGyro[0] - refGyro[0]);
                if (rotationDiff > 1.0f) {
                    currentPattern.setErrorType(MotionPattern.ERROR_WRONG_ANGLE);
                }
                break;

            case EXERCISE_PUSHUP:
                // Similar checks for pushup form
                // Would be expanded in a real implementation
                break;

            default:
                // Default error type
                currentPattern.setErrorType(MotionPattern.ERROR_INCOMPLETE_RANGE);
        }
    }

    /**
     * Get feedback based on detected motion pattern
     * @param pattern Detected motion pattern
     * @param exerciseType Type of exercise being performed
     * @return Feedback message
     */
    public String getMotionFeedback(MotionPattern pattern, int exerciseType) {
        // If pattern is correct, no feedback needed
        if (pattern.getType() == MotionPattern.TYPE_CORRECT) {
            return "Good form";
        }

        // Otherwise, provide feedback based on error type
        switch (pattern.getErrorType()) {
            case MotionPattern.ERROR_TOO_FAST:
                return "Slow down your movement";

            case MotionPattern.ERROR_TOO_SLOW:
                return "Try to maintain a steady pace";

            case MotionPattern.ERROR_WRONG_ANGLE:
                if (exerciseType == EXERCISE_SQUAT) {
                    return "Keep your back straight and knees aligned";
                } else if (exerciseType == EXERCISE_PUSHUP) {
                    return "Lower your body in a straight line";
                } else {
                    return "Check your form alignment";
                }

            case MotionPattern.ERROR_INCOMPLETE_RANGE:
                if (exerciseType == EXERCISE_SQUAT) {
                    return "Go deeper in your squat";
                } else if (exerciseType == EXERCISE_PUSHUP) {
                    return "Lower your chest closer to the ground";
                } else {
                    return "Complete the full range of motion";
                }

            default:
                return "Maintain proper form";
        }
    }

    /**
     * Detect repetitions from a sequence of motion patterns
     * @param patterns List of detected patterns over time
     * @return Number of repetitions detected
     */
    public int countRepetitions(List<MotionPattern> patterns) {
        // Simplified repetition counting
        // In a real implementation, this would use more sophisticated algorithms

        int repCount = 0;
        boolean inRep = false;

        // Scan through patterns to detect repetition cycles
        for (int i = 0; i < patterns.size(); i++) {
            MotionPattern pattern = patterns.get(i);
            float[] acc = pattern.getAccelerationData();

            // Detect start of repetition (e.g., downward motion in squat)
            if (!inRep && acc[1] < -1.0f) {
                inRep = true;
            }

            // Detect end of repetition (e.g., upward motion completing squat)
            else if (inRep && acc[1] > 1.0f) {
                inRep = false;
                repCount++;
            }
        }

        return repCount;
    }
}