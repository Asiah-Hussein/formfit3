package com.asiah.formfit.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.asiah.formfit.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Simple Progress Activity
 */
public class ProgressActivity extends Activity {

    private TextView tvWeeklyExercises, tvTotalExercises, tvAvgAccuracy, tvLastExercise;
    private TextView tvAchievement1, tvAchievement2, tvAchievement3;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        prefs = getSharedPreferences("FormFitData", MODE_PRIVATE);

        initViews();
        loadStats();
        setupClickListeners();
    }

    private void initViews() {
        tvWeeklyExercises = findViewById(R.id.tvWeeklyExercises);
        tvTotalExercises = findViewById(R.id.tvTotalExercises);
        tvAvgAccuracy = findViewById(R.id.tvAvgAccuracy);
        tvLastExercise = findViewById(R.id.tvLastExercise);

        tvAchievement1 = findViewById(R.id.tvAchievement1);
        tvAchievement2 = findViewById(R.id.tvAchievement2);
        tvAchievement3 = findViewById(R.id.tvAchievement3);

        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void loadStats() {
        // Load stats from SharedPreferences
        int totalExercises = prefs.getInt("total_exercises", 0);
        int weeklyExercises = Math.min(totalExercises, 23); // Cap at 23 for demo
        float avgAccuracy = prefs.getFloat("avg_accuracy", 0f);

        String lastExercise = prefs.getString("last_exercise", "None");
        float lastAccuracy = prefs.getFloat("last_accuracy", 0f);
        int lastCalories = prefs.getInt("last_calories", 0);
        int lastReps = prefs.getInt("last_reps", 0);
        long lastTime = prefs.getLong("last_time", 0);

        // Display stats
        tvWeeklyExercises.setText(String.valueOf(weeklyExercises));
        tvTotalExercises.setText(String.valueOf(Math.max(totalExercises, 1)));

        if (avgAccuracy > 0) {
            tvAvgAccuracy.setText(String.format("%.1f%%", avgAccuracy));
        } else {
            tvAvgAccuracy.setText("90.0%");
        }

        // Last exercise info
        if (lastTime > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
            String timeStr = sdf.format(new Date(lastTime));
            tvLastExercise.setText(String.format("%s\n%.0f%% • %d cal • %d reps\n%s",
                    lastExercise, lastAccuracy, lastCalories, lastReps, timeStr));
        } else {
            tvLastExercise.setText("No recent exercises");
        }

        // Simple achievements
        if (totalExercises >= 1) {
            tvAchievement1.setText("🎯 First Workout\nCompleted!");
        } else {
            tvAchievement1.setText("🎯 First Workout\nNot yet");
        }

        if (avgAccuracy >= 95) {
            tvAchievement2.setText("⭐ Perfect Form\nAchieved!");
        } else {
            tvAchievement2.setText("⭐ Perfect Form\nGet 95%+ accuracy");
        }

        if (weeklyExercises >= 7) {
            tvAchievement3.setText("🔥 Active Week\nCompleted!");
        } else {
            tvAchievement3.setText("🔥 Active Week\n" + (7 - weeklyExercises) + " more to go");
        }
    }

    private void setupClickListeners() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        btnExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToExerciseLibrary();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Settings not implemented for prototype
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ProgressActivity.this, ExerciseSetupActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToExerciseLibrary() {
        Intent intent = new Intent(ProgressActivity.this, ExerciseLibraryActivity.class);
        startActivity(intent);
        finish();
    }
}