package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.R;

/**
 * ProgressActivity displays the user's exercise history and achievements.
 * Simplified version for prototype without complex charting.
 */
public class ProgressActivity extends AppCompatActivity {

    private TextView tvExerciseCount;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    // Sample data for prototype
    private int weeklyExerciseCount = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Initialize UI components
        initViews();

        // Set up exercise count
        tvExerciseCount.setText(String.valueOf(weeklyExerciseCount));

        // Set up navigation listeners
        setupNavigationListeners();
    }

    private void initViews() {
        tvExerciseCount = findViewById(R.id.tvExerciseCount);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupNavigationListeners() {
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