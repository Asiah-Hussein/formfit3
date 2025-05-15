package com.asiah.formfit.ml;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * PoseAnalyzer uses ML Kit to detect and analyze human poses for exercise form evaluation
 */
public class PoseAnalyzer {

    private static final String TAG = "PoseAnalyzer";

    // ML Kit pose detector
    private PoseDetector poseDetector;

    // Executor for background processing
    private Executor executor;

    // Listener for pose detection results
    private PoseListener poseListener;

    /**
     * Create a new pose analyzer with ML Kit
     */
    public PoseAnalyzer(Context context) {
        // Configure pose detector options
        AccuratePoseDetectorOptions options = new AccuratePoseDetectorOptions.Builder()
                .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                .build();

        // Create the pose detector
        poseDetector = PoseDetection.getClient(options);

        // Create executor for background processing
        executor = Executors.newSingleThreadExecutor();

        Log.d(TAG, "PoseAnalyzer initialized with ML Kit");
    }

    /**
     * Process a bitmap image for pose detection
     */
    public void analyze(final InputImage image) {
        if (image == null) {
            Log.e(TAG, "Cannot analyze null image");
            return;
        }

        // Process image in background
        executor.execute(() -> {
            // Process the image with ML Kit
            poseDetector.process(image)
                    .addOnSuccessListener(new OnSuccessListener<Pose>() {
                        @Override
                        public void onSuccess(Pose pose) {
                            if (poseListener != null) {
                                poseListener.onPoseDetected(pose);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Pose detection failed: " + e.getMessage());
                            if (poseListener != null) {
                                poseListener.onPoseDetectionFailed(e.getMessage());
                            }
                        }
                    });
        });
    }

    /**
     * Analyze pose for exercise form
     */
    public ExerciseFormResult analyzeExerciseForm(Pose pose, int exerciseType) {
        ExerciseFormResult result = new ExerciseFormResult();
        result.setExerciseType(exerciseType);

        if (pose == null) {
            Log.e(TAG, "Cannot analyze null pose");
            result.setFormQuality(ExerciseFormResult.FORM_UNKNOWN);
            result.setConfidence(0.0f);
            return result;
        }

        // Get key landmarks
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW);
        PoseLandmark rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW);
        PoseLandmark leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST);
        PoseLandmark rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        // Check if key landmarks are detected
        if (leftShoulder == null || rightShoulder == null ||
                leftHip == null || rightHip == null ||
                leftKnee == null || rightKnee == null ||
                leftAnkle == null || rightAnkle == null) {

            Log.d(TAG, "Missing key landmarks for form analysis");
            result.setFormQuality(ExerciseFormResult.FORM_UNKNOWN);
            result.setConfidence(0.3f);
            return result;
        }

        // Analyze based on exercise type
        switch (exerciseType) {
            case ExerciseFormResult.EXERCISE_SQUAT:
                return analyzeSquatForm(pose);

            case ExerciseFormResult.EXERCISE_PUSHUP:
                return analyzePushupForm(pose);

            case ExerciseFormResult.EXERCISE_PLANK:
                return analyzePlankForm(pose);

            case ExerciseFormResult.EXERCISE_LUNGE:
                return analyzeLungeForm(pose);

            default:
                Log.d(TAG, "Unknown exercise type: " + exerciseType);
                result.setFormQuality(ExerciseFormResult.FORM_UNKNOWN);
                result.setConfidence(0.5f);
                return result;
        }
    }

    /**
     * Analyze squat form
     */
    private ExerciseFormResult analyzeSquatForm(Pose pose) {
        ExerciseFormResult result = new ExerciseFormResult();
        result.setExerciseType(ExerciseFormResult.EXERCISE_SQUAT);

        // Get key landmarks
        PoseLandmark leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER);
        PoseLandmark rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER);
        PoseLandmark leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP);
        PoseLandmark rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP);
        PoseLandmark leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE);
        PoseLandmark rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE);
        PoseLandmark leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE);
        PoseLandmark rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE);

        // Calculate knee angle (between hip, knee, and ankle)
        double leftKneeAngle = calculateAngle(leftHip, leftKnee, leftAnkle);
        double rightKneeAngle = calculateAngle(rightHip, rightKnee, rightAnkle);
        double avgKneeAngle = (leftKneeAngle + rightKneeAngle) / 2;

        // Calculate hip angle (between shoulder, hip, and knee)
        double leftHipAngle = calculateAngle(leftShoulder, leftHip, leftKnee);
        double rightHipAngle = calculateAngle(rightShoulder, rightHip, rightKnee);
        double avgHipAngle = (leftHipAngle + rightHipAngle) / 2;

        // Check if knees are aligned with ankles (not caving inward)
        boolean kneeAlignment = checkKneeAlignment(leftHip, leftKnee, leftAnkle, rightHip, rightKnee, rightAnkle);

        // Check hip depth (ideal squat has knees at about 90 degrees)
        boolean goodDepth = avgKneeAngle <= 110;

        // Check back angle (should remain relatively straight)
        boolean backStraight = avgHipAngle >= 45 && avgHipAngle <= 90;

        // Determine form quality
        if (goodDepth && kneeAlignment && backStraight) {
            result.setFormQuality(ExerciseFormResult.FORM_CORRECT);
            result.setConfidence(0.9f);
        } else if (!goodDepth) {
            result.setFormQuality(ExerciseFormResult.FORM_ERROR_DEPTH);
            result.setConfidence(0.8f);
        } else if (!kneeAlignment) {
            result.setFormQuality(ExerciseFormResult.FORM_ERROR_KNEE_ALIGNMENT);
            result.setConfidence(0.8f);
        } else if (!backStraight) {
            result.setFormQuality(ExerciseFormResult.FORM_ERROR_BACK_POSTURE);
            result.setConfidence(0.8f);
        } else {
            result.setFormQuality(ExerciseFormResult.FORM_ERROR_GENERAL);
            result.setConfidence(0.6f);
        }

        return result;
    }

    /**
     * Analyze pushup form
     */
    private ExerciseFormResult analyzePushupForm(Pose pose) {
        // For simplicity, we'll provide a simulated pushup analysis for the prototype
        ExerciseFormResult result = new ExerciseFormResult();
        result.setExerciseType(ExerciseFormResult.EXERCISE_PUSHUP);
        result.setFormQuality(ExerciseFormResult.FORM_CORRECT);
        result.setConfidence(0.85f);
        return result;
    }

    /**
     * Analyze plank form
     */
    private ExerciseFormResult analyzePlankForm(Pose pose) {
        // For simplicity, we'll provide a simulated plank analysis for the prototype
        ExerciseFormResult result = new ExerciseFormResult();
        result.setExerciseType(ExerciseFormResult.EXERCISE_PLANK);
        result.setFormQuality(ExerciseFormResult.FORM_CORRECT);
        result.setConfidence(0.85f);
        return result;
    }

    /**
     * Analyze lunge form
     */
    private ExerciseFormResult analyzeLungeForm(Pose pose) {
        // For simplicity, we'll provide a simulated lunge analysis for the prototype
        ExerciseFormResult result = new ExerciseFormResult();
        result.setExerciseType(ExerciseFormResult.EXERCISE_LUNGE);
        result.setFormQuality(ExerciseFormResult.FORM_CORRECT);
        result.setConfidence(0.85f);
        return result;
    }

    /**
     * Calculate angle between three points (in degrees)
     */
    private double calculateAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result = Math.toDegrees(
                Math.atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                        lastPoint.getPosition().x - midPoint.getPosition().x)
                        - Math.atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                        firstPoint.getPosition().x - midPoint.getPosition().x));

        result = Math.abs(result); // Angle should be positive
        if (result > 180) {
            result = 360.0 - result; // Always get the smaller angle
        }

        return result;
    }

    /**
     * Check if points are roughly in a straight line
     */
    private boolean checkStraightLine(PoseLandmark point1, PoseLandmark point2, PoseLandmark point3) {
        // Calculate angles
        double angle = calculateAngle(point1, point2, point3);

        // If angle is close to 180 degrees, points are roughly in a straight line
        return angle >= 160 && angle <= 200;
    }

    /**
     * Check knee alignment (knees should track over toes)
     */
    private boolean checkKneeAlignment(PoseLandmark leftHip, PoseLandmark leftKnee, PoseLandmark leftAnkle,
                                       PoseLandmark rightHip, PoseLandmark rightKnee, PoseLandmark rightAnkle) {
        // Simplified check - in a real implementation, this would be more sophisticated
        return true;
    }

    /**
     * Set pose detection listener
     */
    public void setPoseListener(PoseListener listener) {
        this.poseListener = listener;
    }

    /**
     * Close resources
     */
    public void close() {
        if (poseDetector != null) {
            poseDetector.close();
            poseDetector = null;
        }
    }

    /**
     * Interface for pose detection results
     */
    public interface PoseListener {
        void onPoseDetected(Pose pose);
        void onPoseDetectionFailed(String error);
    }

    /**
     * Class to hold exercise form analysis results
     */
    public static class ExerciseFormResult {
        // Exercise types
        public static final int EXERCISE_UNKNOWN = 0;
        public static final int EXERCISE_SQUAT = 1;
        public static final int EXERCISE_PUSHUP = 2;
        public static final int EXERCISE_PLANK = 3;
        public static final int EXERCISE_LUNGE = 4;

        // Form quality levels
        public static final int FORM_UNKNOWN = 0;
        public static final int FORM_CORRECT = 1;
        public static final int FORM_ERROR_GENERAL = 2;
        public static final int FORM_ERROR_DEPTH = 3;
        public static final int FORM_ERROR_KNEE_ALIGNMENT = 4;
        public static final int FORM_ERROR_BACK_POSTURE = 5;

        private int exerciseType;
        private int formQuality;
        private float confidence;

        public ExerciseFormResult() {
            exerciseType = EXERCISE_UNKNOWN;
            formQuality = FORM_UNKNOWN;
            confidence = 0.0f;
        }

        public int getExerciseType() {
            return exerciseType;
        }

        public void setExerciseType(int exerciseType) {
            this.exerciseType = exerciseType;
        }

        public int getFormQuality() {
            return formQuality;
        }

        public void setFormQuality(int formQuality) {
            this.formQuality = formQuality;
        }

        public float getConfidence() {
            return confidence;
        }

        public void setConfidence(float confidence) {
            this.confidence = confidence;
        }

        public boolean isCorrectForm() {
            return formQuality == FORM_CORRECT;
        }

        public String getFeedbackMessage() {
            switch (formQuality) {
                case FORM_CORRECT:
                    return "Good form! Keep it up.";
                case FORM_ERROR_DEPTH:
                    if (exerciseType == EXERCISE_SQUAT) {
                        return "Try to go deeper in your squat.";
                    } else if (exerciseType == EXERCISE_PUSHUP) {
                        return "Lower your chest closer to the ground.";
                    } else if (exerciseType == EXERCISE_LUNGE) {
                        return "Lower your back knee closer to the ground.";
                    } else {
                        return "Increase your range of motion.";
                    }
                case FORM_ERROR_KNEE_ALIGNMENT:
                    return "Keep your knees in line with your toes.";
                case FORM_ERROR_BACK_POSTURE:
                    return "Keep your back straight throughout the movement.";
                case FORM_ERROR_GENERAL:
                    return "Focus on maintaining proper form.";
                default:
                    return "Continue the exercise.";
            }
        }
    }
}
