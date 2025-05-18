// File: app/src/main/java/com/asiah/formfit/data/FirebaseHelper.java
package com.asiah.formfit.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    // Firebase references
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;
    private DatabaseReference exercisesRef;
    private DatabaseReference achievementsRef;

    // SQLite database helper
    private ExerciseDbHelper dbHelper;

    /**
     * Constructor initializes Firebase references
     */
    public FirebaseHelper(Context context) {
        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
        exercisesRef = database.getReference("exercises");
        achievementsRef = database.getReference("achievements");

        // Initialize SQLite helper
        dbHelper = new ExerciseDbHelper(context);
    }

    /**
     * Get the current Firebase user ID
     */
    public String getCurrentUserId() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    /**
     * Sync user data to Firebase
     */
    public void syncUser(User user, SyncListener listener) {
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create a map of user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("joinDate", user.getJoinDate().getTime());

        // Save to Firebase
        usersRef.child(firebaseUserId).updateChildren(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User data synced successfully");
                        if (listener != null) {
                            listener.onSyncComplete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error syncing user data: " + e.getMessage());
                        if (listener != null) {
                            listener.onSyncFailed(e.getMessage());
                        }
                    }
                });
    }

    /**
     * Sync exercise data to Firebase
     */
    public void syncExercise(Exercise exercise, SyncListener listener) {
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create unique key for this exercise
        String exerciseKey = exercisesRef.child(firebaseUserId).push().getKey();

        // Create a map of exercise data
        Map<String, Object> exerciseData = new HashMap<>();
        exerciseData.put("name", exercise.getName());
        exerciseData.put("duration", exercise.getDuration());
        exerciseData.put("formAccuracy", exercise.getFormAccuracy());
        exerciseData.put("reps", exercise.getReps());
        exerciseData.put("calories", exercise.getCalories());
        exerciseData.put("timestamp", exercise.getTimestamp().getTime());

        // Save to Firebase
        exercisesRef.child(firebaseUserId).child(exerciseKey).setValue(exerciseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Exercise data synced successfully");
                        // Mark as synced in local database
                        dbHelper.markExerciseSynced(exercise.getId());
                        if (listener != null) {
                            listener.onSyncComplete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error syncing exercise data: " + e.getMessage());
                        if (listener != null) {
                            listener.onSyncFailed(e.getMessage());
                        }
                    }
                });
    }

    /**
     * Sync achievement data to Firebase
     */
    public void syncAchievement(Achievement achievement, SyncListener listener) {
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create unique key for this achievement
        String achievementKey = achievementsRef.child(firebaseUserId).push().getKey();

        // Create a map of achievement data
        Map<String, Object> achievementData = new HashMap<>();
        achievementData.put("name", achievement.getName());
        achievementData.put("description", achievement.getDescription());
        achievementData.put("date", achievement.getDate().getTime());

        // Save to Firebase
        achievementsRef.child(firebaseUserId).child(achievementKey).setValue(achievementData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Achievement data synced successfully");
                        if (listener != null) {
                            listener.onSyncComplete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error syncing achievement data: " + e.getMessage());
                        if (listener != null) {
                            listener.onSyncFailed(e.getMessage());
                        }
                    }
                });
    }

    public interface SyncListener {
        void onSyncComplete();
        void onSyncFailed(String error);
    }

    public interface FirebaseDataListener<T> {
        void onDataLoaded(T data);
        void onDataFailed(String error);
    }
}