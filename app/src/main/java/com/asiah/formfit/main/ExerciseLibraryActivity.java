package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ExerciseLibraryActivity displays the available exercises categorized by body parts.
 * Users can browse, search, and select exercises to start their workouts.
 */
public class ExerciseLibraryActivity extends AppCompatActivity {

    private RecyclerView rvExercises;
    private EditText etSearch;
    private TabLayout tabLayout;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    private ExerciseAdapter exerciseAdapter;
    private List<Exercise> allExercises;
    private List<Exercise> filteredExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_library);

        // Initialize UI components
        rvExercises = findViewById(R.id.rvExercises);
        etSearch = findViewById(R.id.etSearch);
        tabLayout = findViewById(R.id.tabLayout);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);

        // Generate sample exercises
        generateSampleExercises();

        // Set up RecyclerView
        setupRecyclerView();

        // Set up search functionality
        setupSearch();

        // Set up tab selection
        setupTabs();

        // Set up navigation
        setupNavigationListeners();
    }

    private void generateSampleExercises() {
        // In a real app, this would load from a database
        allExercises = new ArrayList<>();

        // Lower Body Exercises
        allExercises.add(new Exercise("Squat", "Lower Body", "Beginner", R.drawable.ic_squat));
        allExercises.add(new Exercise("Lunges", "Lower Body", "Beginner", R.drawable.ic_lunges));
        allExercises.add(new Exercise("Deadlift", "Lower Body", "Intermediate", R.drawable.ic_deadlift));
        allExercises.add(new Exercise("Leg Press", "Lower Body", "Beginner", R.drawable.ic_leg_press));

        // Upper Body Exercises
        allExercises.add(new Exercise("Push-up", "Upper Body", "Beginner", R.drawable.ic_pushup));
        allExercises.add(new Exercise("Pull-up", "Upper Body", "Intermediate", R.drawable.ic_pullup));
        allExercises.add(new Exercise("Bench Press", "Upper Body", "Intermediate", R.drawable.ic_bench_press));
        allExercises.add(new Exercise("Shoulder Press", "Upper Body", "Intermediate", R.drawable.ic_shoulder_press));

        // Core Exercises
        allExercises.add(new Exercise("Plank", "Core", "Beginner", R.drawable.ic_plank));
        allExercises.add(new Exercise("Crunches", "Core", "Beginner", R.drawable.ic_crunches));
        allExercises.add(new Exercise("Russian Twist", "Core", "Intermediate", R.drawable.ic_russian_twist));

        // Cardio Exercises
        allExercises.add(new Exercise("Jumping Jacks", "Cardio", "Beginner", R.drawable.ic_jumping_jacks));
        allExercises.add(new Exercise("Burpees", "Cardio", "Advanced", R.drawable.ic_burpees));
        allExercises.add(new Exercise("Mountain Climbers", "Cardio", "Intermediate", R.drawable.ic_mountain_climbers));

        // Initialize filtered list with all exercises
        filteredExercises = new ArrayList<>(allExercises);
    }

    private void setupRecyclerView() {
        exerciseAdapter = new ExerciseAdapter(filteredExercises);
        rvExercises.setAdapter(exerciseAdapter);
        rvExercises.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter exercises based on search query
                filterExercises(s.toString(), getSelectedCategory());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed
            }
        });
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Filter exercises based on selected category
                filterExercises(etSearch.getText().toString(), getSelectedCategory());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Not needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Not needed
            }
        });
    }

    private String getSelectedCategory() {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();
        switch (selectedTabPosition) {
            case 0: return "All"; // All exercises
            case 1: return "Upper Body";
            case 2: return "Lower Body";
            case 3: return "Core";
            case 4: return "Cardio";
            default: return "All";
        }
    }

    private void filterExercises(String query, String category) {
        filteredExercises.clear();

        for (Exercise exercise : allExercises) {
            boolean matchesQuery = query.isEmpty() ||
                    exercise.getName().toLowerCase().contains(query.toLowerCase());

            boolean matchesCategory = category.equals("All") ||
                    exercise.getCategory().equals(category);

            if (matchesQuery && matchesCategory) {
                filteredExercises.add(exercise);
            }
        }

        exerciseAdapter.notifyDataSetChanged();
    }

    private void setupNavigationListeners() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        // Exercises button is already active

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProgress();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For prototype, we won't implement settings
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

    private void startExercise(Exercise exercise) {
        Intent intent = new Intent(ExerciseLibraryActivity.this, ExerciseSetupActivity.class);
        intent.putExtra("EXERCISE_NAME", exercise.getName());
        startActivity(intent);
    }

    // Exercise model class
    public static class Exercise {
        private String name;
        private String category;
        private String difficulty;
        private int iconResource;

        public Exercise(String name, String category, String difficulty, int iconResource) {
            this.name = name;
            this.category = category;
            this.difficulty = difficulty;
            this.iconResource = iconResource;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public String getDifficulty() {
            return difficulty;
        }

        public int getIconResource() {
            return iconResource;
        }
    }

    // RecyclerView Adapter for Exercises
    private class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

        private List<Exercise> exercises;

        public ExerciseAdapter(List<Exercise> exercises) {
            this.exercises = exercises;
        }

        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_exercise, parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
            Exercise exercise = exercises.get(position);
            holder.bind(exercise);
        }

        @Override
        public int getItemCount() {
            return exercises.size();
        }

        class ExerciseViewHolder extends RecyclerView.ViewHolder {
            private ImageView ivExerciseIcon;
            private TextView tvExerciseName, tvExerciseCategory, tvDifficulty;
            private ImageButton btnStartExercise;

            public ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                ivExerciseIcon = itemView.findViewById(R.id.ivExerciseIcon);
                tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
                tvExerciseCategory = itemView.findViewById(R.id.tvExerciseCategory);
                tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
                btnStartExercise = itemView.findViewById(R.id.btnStartExercise);
            }

            public void bind(final Exercise exercise) {
                tvExerciseName.setText(exercise.getName());
                tvExerciseCategory.setText(exercise.getCategory());
                tvDifficulty.setText(exercise.getDifficulty());
                ivExerciseIcon.setImageResource(exercise.getIconResource());

                btnStartExercise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startExercise(exercise);
                    }
                });
            }
        }
    }
}
