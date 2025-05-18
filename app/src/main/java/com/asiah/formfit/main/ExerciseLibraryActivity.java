package com.asiah.formfit.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.asiah.formfit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple Exercise Library Activity
 */
public class ExerciseLibraryActivity extends Activity {

    private ListView lvExercises;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    private List<String> exerciseList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_library);

        initViews();
        setupExerciseList();
        setupClickListeners();
    }

    private void initViews() {
        lvExercises = findViewById(R.id.lvExercises);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);
    }

    private void setupExerciseList() {
        exerciseList = new ArrayList<>();

        // Add exercises by category
        exerciseList.add("📚 UPPER BODY");
        exerciseList.add("   Push-ups - Beginner");
        exerciseList.add("   Pull-ups - Intermediate");
        exerciseList.add("   Bench Press - Intermediate");
        exerciseList.add("   Shoulder Press - Intermediate");

        exerciseList.add("📚 LOWER BODY");
        exerciseList.add("   Squats - Beginner");
        exerciseList.add("   Lunges - Beginner");
        exerciseList.add("   Deadlifts - Advanced");

        exerciseList.add("📚 CORE");
        exerciseList.add("   Planks - Beginner");
        exerciseList.add("   Crunches - Beginner");
        exerciseList.add("   Russian Twist - Intermediate");

        exerciseList.add("📚 CARDIO");
        exerciseList.add("   Jumping Jacks - Beginner");
        exerciseList.add("   Burpees - Advanced");
        exerciseList.add("   Mountain Climbers - Intermediate");

        // Set up adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exerciseList);
        lvExercises.setAdapter(adapter);

        // Set click listener for exercises
        lvExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = exerciseList.get(position);

                // Only start exercise if it's not a category header
                if (!selectedItem.startsWith("📚")) {
                    String exerciseName = selectedItem.trim().split(" - ")[0].replace("   ", "");
                    startExercise(exerciseName);
                }
            }
        });
    }

    private void startExercise(String exerciseName) {
        Intent intent = new Intent(ExerciseLibraryActivity.this, ExerciseSetupActivity.class);
        intent.putExtra("SELECTED_EXERCISE", exerciseName);
        startActivity(intent);
        Toast.makeText(this, "Starting " + exerciseName + "...", Toast.LENGTH_SHORT).show();
    }

    private void setupClickListeners() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
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
                Toast.makeText(ExerciseLibraryActivity.this, "Settings coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ExerciseLibraryActivity.this, ExerciseSetupActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToProgress() {
        Intent intent = new Intent(ExerciseLibraryActivity.this, ProgressActivity.class);
        startActivity(intent);
        finish();
    }
}
