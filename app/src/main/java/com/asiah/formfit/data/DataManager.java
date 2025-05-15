package com.asiah.formfit.data;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * DataManager implements the hybrid persistence strategy for Form-Fit.
 * It coordinates between local SQLite storage and Firebase cloud synchronization.
 */
public class DataManager {

    private static final String TAG = "DataManager";

    // Database helpers
    private ExerciseDbHelper dbHelper;
    private FirebaseHelper firebaseHelper;

    // Current user
    private long localUserId;
    private User currentUser;

    // Singleton instance
    private static DataManager instance;

    // Data listener
    public interface DataListener<T> {
        void onDataLoaded(T data);
        void onDataFailed(String error);
    }

    /**
     * Get the singleton instance of DataManager
     */
    public static synchronized DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Private constructor for singleton pattern
     */
    private DataManager(Context context) {
        dbHelper = new ExerciseDbHelper(context);
        firebaseHelper = new FirebaseHelper(context);
    }

    /**
     * Initialize the current user
     */
    public void initializeUser(String username, String email, final DataListener<User> listener) {
        // Create a new user
        final User user = new User(username, email);

        // First, try to save to local database
        long userId = dbHelper.addUser(user);

        if (userId > 0) {
            // Local save successful
            user.setId(userId);
            localUserId = userId;
            currentUser = user;

            // Now sync to Firebase if connected
            if (isFirebaseAuthenticated()) {
                firebaseHelper.syncUser(user, new FirebaseHelper.SyncListener() {
                    @Override
                    public void onSyncComplete() {
                        Log.d(TAG, "User synced to Firebase");
                        if (listener != null) {
                            listener.onDataLoaded(user);
                        }
                    }

                    @Override
                    public void onSyncFailed(String error) {
                        Log.e(TAG, "Failed to sync user: " + error);
                        // Still return success since local save worked
                        if (listener != null) {
                            listener.onDataLoaded(user);
                        }
                    }
                });
            } else {
                // Not authenticated, but local save was successful
                if (listener != null) {
                    listener.onDataLoaded(user);
                }
            }
        } else {
            // Failed to save locally
            if (listener != null) {
                listener.onDataFailed("Failed to save user locally");
            }
        }
    }

    /**
     * Save an exercise session
     */
    public void saveExercise(String name, int duration, float formAccuracy, int reps, int calories, final DataListener<Exercise> listener) {
        // Create a new exercise
        final Exercise exercise = new Exercise(localUserId, name, duration, formAccuracy, reps, calories);

        // First, save to local database
        long exerciseId = dbHelper.addExercise(exercise);

        if (exerciseId > 0) {
            // Local save successful
            exercise.setId(exerciseId);

            // Now sync to Firebase if connected
            if (isFirebaseAuthenticated()) {
                firebaseHelper.syncExercise(exercise, new FirebaseHelper.SyncListener() {
                    @Override
                    public void onSyncComplete() {
                        Log.d(TAG, "Exercise synced to Firebase");
                        exercise.setSynced(true);
                        if (listener != null) {
                            listener.onDataLoaded(exercise);
                        }
                    }

                    @Override
                    public void onSyncFailed(String error) {
                        Log.e(TAG, "Failed to sync exercise: " + error);
                        // Still return success since local save worked
                        if (listener != null) {
                            listener.onDataLoaded(exercise);
                        }
                    }
                });
            } else {
                // Not authenticated, but local save was successful
                if (listener != null) {
                    listener.onDataLoaded(exercise);
                }
            }
        } else {
            // Failed to save locally
            if (listener != null) {
                listener.onDataFailed("Failed to save exercise locally");
            }
        }
    }

    /**
     * Save an achievement
     */
    public void saveAchievement(String name, String description, final DataListener<Achievement> listener) {
        // Create a new achievement
        final Achievement achievement = new Achievement(localUserId, name, description);

        // First, save to local database
        long achievementId = dbHelper.addAchievement(achievement);

        if (achievementId > 0) {
            // Local save successful
            achievement.setId(achievementId);

            // Now sync to Firebase if connected
            if (isFirebaseAuthenticated()) {
                firebaseHelper.syncAchievement(achievement, new FirebaseHelper.SyncListener() {
                    @Override
                    public void onSyncComplete() {
                        Log.d(TAG, "Achievement synced to Firebase");
                        if (listener != null) {
                            listener.onDataLoaded(achievement);
                        }
                    }

                    @Override
                    public void onSyncFailed(String error) {
                        Log.e(TAG, "Failed to sync achievement: " + error);
                        // Still return success since local save worked
                        if (listener != null) {
                            listener.onDataLoaded(achievement);
                        }
                    }
                });
            } else {
                // Not authenticated, but local save was successful
                if (listener != null) {
                    listener.onDataLoaded(achievement);
                }
            }
        } else {
            // Failed to save locally
            if (listener != null) {
                listener.onDataFailed("Failed to save achievement locally");
            }
        }
    }

    /**
     * Get user exercises
     */
    public void getUserExercises(final DataListener<List<Exercise>> listener) {
        // First try to get from local database
        final List<Exercise> localExercises = dbHelper.getUserExercises(localUserId);

        // Now try to get from Firebase for the most updated data
        if (isFirebaseAuthenticated()) {
            firebaseHelper.loadUserExercises(new FirebaseHelper.FirebaseDataListener<List<Exercise>>() {
                @Override
                public void onDataLoaded(List<Exercise> firebaseExercises) {
                    // Merge local and Firebase exercises
                    // In a complete implementation, you would need to handle conflicts
                    // For prototype purposes, we'll just return the Firebase exercises if available
                    if (listener != null) {
                        listener.onDataLoaded(firebaseExercises);
                    }
                }

                @Override
                public void onDataFailed(String error) {
                    Log.e(TAG, "Failed to get exercises from Firebase: " + error);
                    // Return local exercises as fallback
                    if (listener != null) {
                        listener.onDataLoaded(localExercises);
                    }
                }
            });
        } else {
            // Not authenticated, return local exercises
            if (listener != null) {
                listener.onDataLoaded(localExercises);
            }
        }
    }

    /**
     * Get user achievements
     */
    public void getUserAchievements(final DataListener<List<Achievement>> listener) {
        // Get from local database
        List<Achievement> achievements = dbHelper.getUserAchievements(localUserId);

        // Return achievements
        if (listener != null) {
            listener.onDataLoaded(achievements);
        }
    }

    /**
     * Sync all unsynced data to the cloud
     */
    public void syncAllData(final DataListener<Boolean> listener) {
        if (isFirebaseAuthenticated()) {
            firebaseHelper.syncUnsyncedExercises(new FirebaseHelper.SyncListener() {
                @Override
                public void onSyncComplete() {
                    Log.d(TAG, "All data synced successfully");
                    if (listener != null) {
                        listener.onDataLoaded(true);
                    }
                }

                @Override
                public void onSyncFailed(String error) {
                    Log.e(TAG, "Failed to sync all data: " + error);
                    if (listener != null) {
                        listener.onDataFailed(error);
                    }
                }
            });
        } else {
            // Not authenticated
            if (listener != null) {
                listener.onDataFailed("Not authenticated with Firebase");
            }
        }
    }

    /**
     * Check if user is authenticated with Firebase
     */
    public boolean isFirebaseAuthenticated() {
        return firebaseHelper.getCurrentUserId() != null;
    }

    /**
     * Set the current local user ID
     */
    public void setLocalUserId(long userId) {
        this.localUserId = userId;
    }

    /**
     * Get the current local user ID
     */
    public long getLocalUserId() {
        return localUserId;
    }
}
