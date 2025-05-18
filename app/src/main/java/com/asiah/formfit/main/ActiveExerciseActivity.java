package com.asiah.formfit.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.asiah.formfit.R;

/**
 * ActiveExerciseActivity - Fixed to extend Activity instead of AppCompatActivity
 */
public class ActiveExerciseActivity extends Activity {

    private TextView tvExerciseName;
    private TextView tvFormAccuracy;
    private TextView tvCaloriesBurned;
    private TextView tvRepCount;
    private TextView tvFormCorrection;
    private ImageButton btnClose;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    private Handler handler = new Handler();
    private boolean isExercising = true;
    private String exerciseName = "Exercise";

    // Simple exercise data
    private float formAccuracy = 92f;
    private int calories = 148;
    private int reps = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_exercise);

        initViews();
        setupExercise();
        setupClickListeners();
        startSimulation();
    }

    private void initViews() {
        // Find all views safely
        tvExerciseName = findViewById(R.id.tvExerciseName);
        tvFormAccuracy = findViewById(R.id.tvFormAccuracy);
        tvCaloriesBurned = findViewById(R.id.tvCaloriesBurned);
        tvRepCount = findViewById(R.id.tvRepCount);
        tvFormCorrection = findViewById(R.id.tvFormCorrection);
        btnClose = findViewById(R.id.btnClose);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupExercise() {
        // Get exercise name from intent
        exerciseName = getIntent().getStringExtra("EXERCISE_TYPE");
        if (exerciseName == null || exerciseName.equals("Select Exercise")) {
            exerciseName = "Squat Form";
        }

        // Set initial values
        tvExerciseName.setText(exerciseName);
        tvFormAccuracy.setText("92%");
        tvCaloriesBurned.setText("148");
        tvRepCount.setText("12");
        tvFormCorrection.setText("Excellent form! Keep it up!");
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishExercise();
            }
        });

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

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProgress();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActiveExerciseActivity.this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSimulation() {
        Toast.makeText(this, "Starting " + exerciseName + "...", Toast.LENGTH_LONG).show();

        // Update every 3 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isExercising) {
                    updateStats();
                    handler.postDelayed(this, 3000);
                }
            }
        }, 3000);
    }

    private void updateStats() {
        // Simulate changing values
        formAccuracy += (Math.random() - 0.5) * 6;
        formAccuracy = Math.max(80, Math.min(98, formAccuracy));

        if (Math.random() > 0.4) {
            calories += (int)(Math.random() * 4) + 1;
            reps += 1;
        }

        // Update UI
        tvFormAccuracy.setText(String.format("%.0f%%", formAccuracy));
        tvCaloriesBurned.setText(String.valueOf(calories));
        tvRepCount.setText(String.valueOf(reps));

        // Update feedback
        if (formAccuracy > 90) {
            tvFormCorrection.setText("Excellent form! Keep it up! 💪");
        } else if (formAccuracy > 85) {
            tvFormCorrection.setText("Good form. Stay focused! 👍");
        } else {
            tvFormCorrection.setText("Focus on your form ⚠️");
        }
    }

    private void finishExercise() {
        isExercising = false;
        handler.removeCallbacksAndMessages(null);

        // Save exercise data
        saveExerciseData();

        Toast.makeText(this,
                String.format("Exercise Complete!\n%d reps • %.0f%% form • %d calories",
                        reps, formAccuracy, calories),
                Toast.LENGTH_LONG).show();

        finish();
    }

    private void saveExerciseData() {
        SharedPreferences prefs = getSharedPreferences("FormFitData", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Save session data
        int totalExercises = prefs.getInt("total_exercises", 0) + 1;
        editor.putInt("total_exercises", totalExercises);
        editor.putString("last_exercise", exerciseName);
        editor.putFloat("last_accuracy", formAccuracy);
        editor.putInt("last_calories", calories);
        editor.putInt("last_reps", reps);
        editor.putLong("last_time", System.currentTimeMillis());

        editor.apply();
    }

    private void navigateToHome() {
        Intent intent = new Intent(ActiveExerciseActivity.this, ExerciseSetupActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToExerciseLibrary() {
        Intent intent = new Intent(ActiveExerciseActivity.this, ExerciseLibraryActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToProgress() {
        Intent intent = new Intent(ActiveExerciseActivity.this, ProgressActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isExercising = false;
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Use the close button to finish exercise", Toast.LENGTH_SHORT).show();
    }
}