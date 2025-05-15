package com.asiah.formfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asiah.formfit.R;
import com.asiah.formfit.data.Exercise;

import java.util.List;

/**
 * Adapter for displaying exercises in a RecyclerView
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exercises;
    private OnExerciseClickListener listener;

    /**
     * Interface for exercise item click events
     */
    public interface OnExerciseClickListener {
        void onExerciseClick(Exercise exercise);
        void onStartExerciseClick(Exercise exercise);
    }

    public ExerciseAdapter(List<Exercise> exercises, OnExerciseClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
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
        holder.bind(exercise, listener);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    /**
     * Update the exercise list and refresh the view
     */
    public void updateExercises(List<Exercise> newExercises) {
        this.exercises = newExercises;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for exercise items
     */
    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivExerciseIcon;
        private TextView tvExerciseName;
        private TextView tvExerciseCategory;
        private TextView tvDifficulty;
        private ImageButton btnStartExercise;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            ivExerciseIcon = itemView.findViewById(R.id.ivExerciseIcon);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvExerciseCategory = itemView.findViewById(R.id.tvExerciseCategory);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            btnStartExercise = itemView.findViewById(R.id.btnStartExercise);
        }

        public void bind(final Exercise exercise, final OnExerciseClickListener listener) {
            // Set exercise details
            tvExerciseName.setText(exercise.getName());
            tvExerciseCategory.setText(exercise.getCategory());

            // Set difficulty level
            String difficultyLevel;
            switch (exercise.getDifficulty()) {
                case Exercise.DIFFICULTY_BEGINNER:
                    difficultyLevel = "Beginner";
                    break;
                case Exercise.DIFFICULTY_INTERMEDIATE:
                    difficultyLevel = "Intermediate";
                    break;
                case Exercise.DIFFICULTY_ADVANCED:
                    difficultyLevel = "Advanced";
                    break;
                default:
                    difficultyLevel = "Beginner";
            }
            tvDifficulty.setText(difficultyLevel);

            // Set exercise icon
            if (exercise.getIconResourceId() != 0) {
                ivExerciseIcon.setImageResource(exercise.getIconResourceId());
            } else {
                // Default icon if none specified
                ivExerciseIcon.setImageResource(R.drawable.ic_exercise_default);
            }

            // Set click listeners
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onExerciseClick(exercise);
                    }
                }
            });

            btnStartExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onStartExerciseClick(exercise);
                    }
                }
            });
        }
    }
}