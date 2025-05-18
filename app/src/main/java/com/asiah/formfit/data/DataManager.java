package com.asiah.formfit.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified DataManager for the prototype
 */
public class DataManager {

    private static final String TAG = "DataManager";
    private static DataManager instance;
    private Context context;

    // Data listener interface
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
        this.context = context;
    }

    /**
     * Initialize the current user (simplified for prototype)
     */
    public void initializeUser(String username, String email, final DataListener<User> listener) {
        // For prototype, just create a user and return success
        User user = new User(username, email);
        user.setId(1); // Hardcoded ID for prototype

        Log.d(TAG, "User initialized: " + username);

        if (listener != null) {
            listener.onDataLoaded(user);
        }
    }

    /**
     * Save an exercise session (simplified for prototype)
     */
    public void saveExercise(String name, int duration, float formAccuracy, int reps, int calories, final DataListener<Exercise> listener) {
        // For prototype, just create an exercise and return success
        Exercise exercise = new Exercise(1, name, duration, formAccuracy, reps, calories);
        exercise.setId(System.currentTimeMillis()); // Use timestamp as ID

        Log.d(TAG, "Exercise saved: " + name);

        if (listener != null) {
            listener.onDataLoaded(exercise);
        }
    }

    /**
     * Save an achievement (simplified for prototype)
     */
    public void saveAchievement(String name, String description, final DataListener<Achievement> listener) {
        // For prototype, just create an achievement and return success
        Achievement achievement = new Achievement(1, name, description);
        achievement.setId(System.currentTimeMillis()); // Use timestamp as ID

        Log.d(TAG, "Achievement saved: " + name);

        if (listener != null) {
            listener.onDataLoaded(achievement);
        }
    }

    /**
     * Get user exercises (returns sample data for prototype)
     */
    public void getUserExercises(final DataListener<List<Exercise>> listener) {
        // For prototype, return sample exercises
        List<Exercise> exercises = new ArrayList<>();
        exercises.add(new Exercise(1, "Squat", 300, 85.5f, 15, 120));
        exercises.add(new Exercise(1, "Push-up", 240, 92.0f, 20, 80));
        exercises.add(new Exercise(1, "Plank", 180, 88.5f, 1, 60));

        Log.d(TAG, "Returning " + exercises.size() + " exercises");

        if (listener != null) {
            listener.onDataLoaded(exercises);
        }
    }

    /**
     * Get user achievements (returns sample data for prototype)
     */
    public void getUserAchievements(final DataListener<List<Achievement>> listener) {
        // For prototype, return sample achievements
        List<Achievement> achievements = new ArrayList<>();
        achievements.add(new Achievement(1, "Perfect Form Master", "Maintained 95%+ form accuracy for 10 consecutive workouts"));
        achievements.add(new Achievement(1, "10 Day Streak", "Exercised for 10 consecutive days"));
        achievements.add(new Achievement(1, "First Workout", "Completed your first workout session"));

        Log.d(TAG, "Returning " + achievements.size() + " achievements");

        if (listener != null) {
            listener.onDataLoaded(achievements);
        }
    }

    /**
     * Check if user is authenticated (always return true for prototype)
     */
    public boolean isFirebaseAuthenticated() {
        return true; // Always authenticated for prototype
    }

    /**
     * Set the current local user ID
     */
    public void setLocalUserId(long userId) {
        // For prototype, just log it
        Log.d(TAG, "Local user ID set to: " + userId);
    }

    /**
     * Get the current local user ID
     */
    public long getLocalUserId() {
        return 1; // Hardcoded for prototype
    }
}
