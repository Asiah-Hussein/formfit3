package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.R;

/**
 * ExerciseSetupActivity - Handles the exercise setup and camera configuration
 * This activity allows users to select an exercise type and configure camera settings
 * before starting their workout session.
 *
 * @author Asiah Abdisalam Hussein
 * @version 1.0
 */
public class ExerciseSetupActivity extends AppCompatActivity {

    // UI Components
    private Button btnMenu;
    private Button btnStartExercise;
    private Button btnNav1, btnNav2, btnNav3, btnNav4;
    private Spinner spinnerExercise;

    // Exercise types available in the app
    private String[] exerciseTypes = {
            "Select Exercise",
            "Squats",
            "Push-ups",
            "Lunges",
            "Bicep Curls",
            "Shoulder Press",
            "Deadlifts"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_setup);

        // Initialize UI components
        initializeViews();

        // Setup exercise spinner
        setupExerciseSpinner();

        // Setup click listeners
        setupClickListeners();
    }

    /**
     * Initialize all UI components by finding them by their IDs
     */
    private void initializeViews() {
        btnMenu = findViewById(R.id.btnMenu);
        btnStartExercise = findViewById(R.id.btnStartExercise);
        btnNav1 = findViewById(R.id.btnNav1);
        btnNav2 = findViewById(R.id.btnNav2);
        btnNav3 = findViewById(R.id.btnNav3);
        btnNav4 = findViewById(R.id.btnNav4);
        spinnerExercise = findViewById(R.id.spinnerExercise);
    }

    /**
     * Setup the exercise type spinner with available exercise options
     */
    private void setupExerciseSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                exerciseTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapter);
    }

    /**
     * Setup click listeners for all interactive components
     */
    private void setupClickListeners() {
        // Menu button listener
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement menu functionality
                Toast.makeText(ExerciseSetupActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Start exercise button listener
        btnStartExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedExercise = spinnerExercise.getSelectedItem().toString();

                if (selectedExercise.equals("Select Exercise")) {
                    Toast.makeText(ExerciseSetupActivity.this,
                            "Please select an exercise first", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Start the selected exercise
                startExercise(selectedExercise);
            }
        });

        // Navigation button listeners
        btnNav1.setOnClickListener(v -> navigateToSection(1));
        btnNav2.setOnClickListener(v -> navigateToSection(2));
        btnNav3.setOnClickListener(v -> navigateToSection(3));
        btnNav4.setOnClickListener(v -> navigateToSection(4));
    }

    /**
     * Start the selected exercise by navigating to the active exercise screen
     * @param exerciseType The type of exercise selected by the user
     */
    private void startExercise(String exerciseType) {
        // Create intent to start the active exercise activity
        Intent intent = new Intent(this, com.asiah.formfit.main.ActiveExerciseActivity.class);
        intent.putExtra("EXERCISE_TYPE", exerciseType);

        // Show loading message
        Toast.makeText(this, "Starting " + exerciseType + "...", Toast.LENGTH_SHORT).show();

        // Start the activity
        startActivity(intent);

        // Add transition animation (optional)
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    /**
     * Handle navigation between different sections of the app
     * @param section The section number to navigate to
     */
    private void navigateToSection(int section) {
        Intent intent;

        switch (section) {
            case 1:
                // Already on exercise setup
                Toast.makeText(this, "Already on Exercise Setup", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // Navigate to active exercise (if available)
                intent = new Intent(this, com.asiah.formfit.main.ActiveExerciseActivity.class);
                startActivity(intent);
                break;
            case 3:
                // Navigate to progress screen
                intent = new Intent(this, com.asiah.formfit.main.ProgressActivity.class);
                startActivity(intent);
                break;
            case 4:
                // Navigate to exercise library
                intent = new Intent(this, com.asiah.formfit.main.ExerciseLibraryActivity.class);
                startActivity(intent);
                break;
            default:
                Toast.makeText(this, "Section not available", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * Initialize camera for exercise form detection
     * This method should be called when camera permissions are granted
     * Note: Camera functionality disabled for prototype
     */
    private void initializeCamera() {
        // TODO: Implement camera initialization
        // This will be used for pose detection during exercises
        // Camera functionality disabled for simplified prototype
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reset navigation highlights
        resetNavigationHighlights();
        btnNav1.setTextColor(getResources().getColor(R.color.colorPrimary, null));
    }

    /**
     * Reset all navigation button highlights
     */
    private void resetNavigationHighlights() {
        int defaultColor = getResources().getColor(R.color.colorHint, null);
        btnNav1.setTextColor(defaultColor);
        btnNav2.setTextColor(defaultColor);
        btnNav3.setTextColor(defaultColor);
        btnNav4.setTextColor(defaultColor);
    }
}