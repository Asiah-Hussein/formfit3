package com.asiah.formfit.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.asiah.formfit.R;

/**
 * ExerciseSetupActivity - Exercise setup and camera configuration
 * Simplified version for prototype demonstration
 */
public class ExerciseSetupActivity extends Activity {

    private Button btnStartExercise;
    private Button btnNavHome, btnNavLibrary, btnNavProgress;
    private Spinner spinnerExercise;

    // Exercise types available
    private String[] exerciseTypes = {
            "Select Exercise",
            "Squats",
            "Push-ups",
            "Lunges",
            "Planks",
            "Jumping Jacks",
            "Burpees"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_setup);

        initializeViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initializeViews() {
        btnStartExercise = findViewById(R.id.btnStartExercise);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavLibrary = findViewById(R.id.btnNavLibrary);
        btnNavProgress = findViewById(R.id.btnNavProgress);
        spinnerExercise = findViewById(R.id.spinnerExercise);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exerciseTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnStartExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedExercise = spinnerExercise.getSelectedItem().toString();

                if (selectedExercise.equals("Select Exercise")) {
                    Toast.makeText(ExerciseSetupActivity.this,
                            "Please select an exercise", Toast.LENGTH_SHORT).show();
                    return;
                }

                startExercise(selectedExercise);
            }
        });

        btnNavHome.setOnClickListener(v -> {
            // Already on home/setup screen
            Toast.makeText(this, "Already on Exercise Setup", Toast.LENGTH_SHORT).show();
        });

        btnNavLibrary.setOnClickListener(v -> navigateToLibrary());
        btnNavProgress.setOnClickListener(v -> navigateToProgress());
    }

    private void startExercise(String exerciseType) {
        try {
            Intent intent = new Intent(this, ActiveExerciseActivity.class);
            intent.putExtra("EXERCISE_TYPE", exerciseType);
            Toast.makeText(this, "Starting " + exerciseType + "...", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Active Exercise coming soon!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToLibrary() {
        try {
            Intent intent = new Intent(this, ExerciseLibraryActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Exercise Library coming soon!", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToProgress() {
        try {
            Intent intent = new Intent(this, ProgressActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Progress Screen coming soon!", Toast.LENGTH_SHORT).show();
        }
    }
}