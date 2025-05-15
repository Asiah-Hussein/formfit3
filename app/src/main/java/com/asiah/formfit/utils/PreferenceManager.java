package com.asiah.formfit.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferenceManager handles application preferences and settings
 */
public class PreferenceManager {

    // Shared preferences file name
    private static final String PREF_NAME = "form_fit_prefs";

    // Preference keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FIRST_LOGIN = "first_login";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_HAPTIC_FEEDBACK_ENABLED = "haptic_feedback_enabled";
    private static final String KEY_CAMERA_FRONT_FACING = "camera_front_facing";
    private static final String KEY_DISPLAY_METRICS_IMPERIAL = "display_metrics_imperial";
    private static final String KEY_DAILY_GOAL = "daily_goal";

    // Singleton instance
    private static PreferenceManager instance;

    // Shared preferences instance
    private SharedPreferences preferences;

    /**
     * Get the singleton instance of PreferenceManager
     */
    public static synchronized PreferenceManager getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Private constructor for singleton pattern
     */
    private PreferenceManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save user ID preference
     */
    public void setUserId(long userId) {
        preferences.edit().putLong(KEY_USER_ID, userId).apply();
    }

    /**
     * Get user ID preference
     */
    public long getUserId() {
        return preferences.getLong(KEY_USER_ID, -1);
    }

    /**
     * Save username preference
     */
    public void setUsername(String username) {
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    /**
     * Get username preference
     */
    public String getUsername() {
        return preferences.getString(KEY_USERNAME, "");
    }

    /**
     * Save email preference
     */
    public void setEmail(String email) {
        preferences.edit().putString(KEY_EMAIL, email).apply();
    }

    /**
     * Get email preference
     */
    public String getEmail() {
        return preferences.getString(KEY_EMAIL, "");
    }

    /**
     * Save first login status
     */
    public void setFirstLogin(boolean isFirstLogin) {
        preferences.edit().putBoolean(KEY_FIRST_LOGIN, isFirstLogin).apply();
    }

    /**
     * Check if this is the first login
     */
    public boolean isFirstLogin() {
        return preferences.getBoolean(KEY_FIRST_LOGIN, true);
    }

    /**
     * Enable or disable notifications
     */
    public void setNotificationsEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }

    /**
     * Check if notifications are enabled
     */
    public boolean areNotificationsEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, true);
    }

    /**
     * Enable or disable haptic feedback
     */
    public void setHapticFeedbackEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_HAPTIC_FEEDBACK_ENABLED, enabled).apply();
    }

    /**
     * Check if haptic feedback is enabled
     */
    public boolean isHapticFeedbackEnabled() {
        return preferences.getBoolean(KEY_HAPTIC_FEEDBACK_ENABLED, true);
    }

    /**
     * Set camera facing direction
     */
    public void setCameraFrontFacing(boolean frontFacing) {
        preferences.edit().putBoolean(KEY_CAMERA_FRONT_FACING, frontFacing).apply();
    }

    /**
     * Check if camera is set to front facing
     */
    public boolean isCameraFrontFacing() {
        return preferences.getBoolean(KEY_CAMERA_FRONT_FACING, true);
    }

    /**
     * Set display metrics to imperial (true) or metric (false)
     */
    public void setDisplayMetricsImperial(boolean imperial) {
        preferences.edit().putBoolean(KEY_DISPLAY_METRICS_IMPERIAL, imperial).apply();
    }

    /**
     * Check if display metrics are imperial
     */
    public boolean isDisplayMetricsImperial() {
        return preferences.getBoolean(KEY_DISPLAY_METRICS_IMPERIAL, true); // Default to imperial for US users
    }

    /**
     * Set daily exercise goal (in minutes)
     */
    public void setDailyGoal(int minutes) {
        preferences.edit().putInt(KEY_DAILY_GOAL, minutes).apply();
    }

    /**
     * Get daily exercise goal
     */
    public int getDailyGoal() {
        return preferences.getInt(KEY_DAILY_GOAL, 30); // Default to 30 minutes
    }

    /**
     * Clear all preferences (e.g. for logout)
     */
    public void clearAll() {
        preferences.edit().clear().apply();
    }
}
