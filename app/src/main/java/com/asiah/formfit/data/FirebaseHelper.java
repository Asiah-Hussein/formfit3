package com.asiah.formfit.data;

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

/**
 * Improved FirebaseHelper implementation for better reliability and error handling
 */
public class FirebaseHelper {

    private static final String TAG = "FirebaseHelper";

    // Firebase references
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    // Database references
    private DatabaseReference usersRef;
    private DatabaseReference exercisesRef;
    private DatabaseReference achievementsRef;

    // Local database helper for syncing
    private ExerciseDbHelper dbHelper;

    // Synchronization listener
    public interface SyncListener {
        void onSyncComplete();
        void onSyncFailed(String error);
    }

    /**
     * Initialize the Firebase helper
     */
    public FirebaseHelper(android.content.Context context) {
        try {
            // Initialize Firebase components
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();

            // Enable disk persistence for offline capability
            firebaseDatabase.setPersistenceEnabled(true);

            // Get database references
            usersRef = firebaseDatabase.getReference("users");
            exercisesRef = firebaseDatabase.getReference("exercises");
            achievementsRef = firebaseDatabase.getReference("achievements");

            // Initialize local database helper
            dbHelper = new ExerciseDbHelper(context);

            Log.d(TAG, "FirebaseHelper initialized successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error initializing FirebaseHelper: " + e.getMessage());
        }
    }

    /**
     * Get the current Firebase user ID
     */
    public String getCurrentUserId() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }

    /**
     * Sign in with email and password
     */
    public void signInWithEmailPassword(String email, String password, final AuthListener listener) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (listener != null) {
                        listener.onAuthSuccess(authResult.getUser());
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onAuthFailed(e.getMessage());
                    }
                });
    }

    /**
     * Create a new user with email and password
     */
    public void createUserWithEmailPassword(String email, String password, final AuthListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    if (listener != null) {
                        listener.onAuthSuccess(authResult.getUser());
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onAuthFailed(e.getMessage());
                    }
                });
    }

    /**
     * Sign out the current user
     */
    public void signOut() {
        firebaseAuth.signOut();
    }

    /**
     * Sync a user to Firebase
     */
    public void syncUser(User user, final SyncListener listener) {
        // Get current Firebase user
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", user.getUsername());
        userData.put("email", user.getEmail());
        userData.put("joinDate", user.getJoinDate().getTime()); // Store as timestamp

        // Update user data in Firebase
        usersRef.child(firebaseUserId).updateChildren(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User data synced successfully");
                    if (listener != null) {
                        listener.onSyncComplete();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync user data: " + e.getMessage());
                    if (listener != null) {
                        listener.onSyncFailed(e.getMessage());
                    }
                });
    }

    /**
     * Sync an exercise to Firebase
     */
    public void syncExercise(final Exercise exercise, final SyncListener listener) {
        // Get current Firebase user
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create exercise data map
        Map<String, Object> exerciseData = new HashMap<>();
        exerciseData.put("userId", firebaseUserId); // Use Firebase user ID
        exerciseData.put("name", exercise.getName());
        exerciseData.put("duration", exercise.getDuration());
        exerciseData.put("formAccuracy", exercise.getFormAccuracy());
        exerciseData.put("reps", exercise.getReps());
        exerciseData.put("calories", exercise.getCalories());
        exerciseData.put("timestamp", exercise.getTimestamp().getTime()); // Store as timestamp

        // Generate a new key for the exercise
        String key = exercisesRef.push().getKey();

        // Update exercise data in Firebase
        exercisesRef.child(key).setValue(exerciseData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Exercise data synced successfully");

                    // Mark as synced in local database
                    dbHelper.markExerciseSynced(exercise.getId());

                    if (listener != null) {
                        listener.onSyncComplete();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync exercise data: " + e.getMessage());
                    if (listener != null) {
                        listener.onSyncFailed(e.getMessage());
                    }
                });
    }

    /**
     * Sync an achievement to Firebase
     */
    public void syncAchievement(Achievement achievement, final SyncListener listener) {
        // Get current Firebase user
        String firebaseUserId = getCurrentUserId();
        if (firebaseUserId == null) {
            if (listener != null) {
                listener.onSyncFailed("User not authenticated");
            }
            return;
        }

        // Create achievement data map
        Map<String, Object> achievementData = new HashMap<>();
        achievementData.put("userId", firebaseUserId); // Use Firebase user ID
        achievementData.put("name", achievement.getName());
        achievementData.put("description", achievement.getDescription());
        achievementData.put("date", achievement.getDate().getTime()); // Store as timestamp

        // Generate a new key for the achievement
        String key = achievementsRef.push().getKey();

        // Update achievement data in Firebase
        achievementsRef.child(key).setValue(achievementData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Achievement data synced successfully");
                    if (listener != null) {
                        listener.onSyncComplete();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to sync achievement data: " + e.getMessage());
                    if (listener != null) {
                        listener.onSyncFailed(e.getMessage());
                    }
                });
    }

    /**
     * Auth listener for Firebase authentication events
     */
    public interface AuthListener {
        void onAuthSuccess(FirebaseUser user);
        void onAuthFailed(String error);
    }

    /**
     * Firebase data listener
     */
    public interface FirebaseDataListener<T> {
        void onDataLoaded(T data);
        void onDataFailed(String error);
    }
}