package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.asiah.formfit.R;

/**
 * ActiveExerciseActivity handles the active exercise session
 * Simplified version for prototype
 */
public class ActiveExerciseActivity extends AppCompatActivity {

    private TextView tvExerciseName;
    private TextView tvFormAccuracy;
    private TextView tvCaloriesBurned;
    private TextView tvRepCount;
    private TextView tvFormCorrection;
    private ImageButton btnClose;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    private Handler handler = new Handler();
    private boolean isExercising = true;

    // Simulated exercise data
    private float formAccuracy = 92f;
    private int calories = 148;
    private int reps = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_exercise);

        initViews();

        // Get exercise name from intent
        String exerciseName = getIntent().getStringExtra("EXERCISE_TYPE");
        if (exerciseName != null) {
            tvExerciseName.setText(exerciseName);
        } else {
            tvExerciseName.setText("Squat Form");
        }

        setupClickListeners();
        startExerciseSimulation();
    }

    private void initViews() {
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
                // Settings not implemented for prototype
            }
        });
    }

    private void startExerciseSimulation() {
        // Update stats every 2 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isExercising) {
                    updateExerciseStats();
                    handler.postDelayed(this, 2000);
                }
            }
        }, 2000);
    }

    private void updateExerciseStats() {
        // Simulate changing form accuracy
        formAccuracy += (Math.random() - 0.5) * 10;
        formAccuracy = Math.max(60, Math.min(100, formAccuracy));

        // Simulate increasing calories and reps
        if (Math.random() > 0.5) {
            calories += (int)(Math.random() * 3);
            reps += (int)(Math.random() * 2);
        }

        // Update UI
        tvFormAccuracy.setText(String.format("%.0f%%", formAccuracy));
        tvCaloriesBurned.setText(String.valueOf(calories));
        tvRepCount.setText(String.valueOf(reps));

        // Update feedback message
        if (formAccuracy > 90) {
            tvFormCorrection.setText("Excellent form! Keep it up!");
        } else if (formAccuracy > 80) {
            tvFormCorrection.setText("Good form. Stay focused.");
        } else if (formAccuracy > 70) {
            tvFormCorrection.setText("Watch your posture");
        } else {
            tvFormCorrection.setText("Keep your back straight and knees aligned");
        }
    }

    private void finishExercise() {
        isExercising = false;
        handler.removeCallbacksAndMessages(null);
        finish();
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
}