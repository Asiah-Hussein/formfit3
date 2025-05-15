package com.asiah.formfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asiah.formfit.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying achievements in a RecyclerView
 */
public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievements;
    private SimpleDateFormat dateFormat;

    public AchievementAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        holder.bind(achievement);
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    /**
     * Update the achievement list and refresh the view
     */
    public void updateAchievements(List<Achievement> newAchievements) {
        this.achievements = newAchievements;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder for achievement items
     */
    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAchievementName;
        private TextView tvAchievementDescription;
        private TextView tvAchievementDate;
        private ImageView ivAchievementIcon;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAchievementName = itemView.findViewById(R.id.tvAchievementName);
            tvAchievementDescription = itemView.findViewById(R.id.tvAchievementDescription);
            tvAchievementDate = itemView.findViewById(R.id.tvAchievementDate);
            ivAchievementIcon = itemView.findViewById(R.id.ivAchievementIcon);
        }

        public void bind(Achievement achievement) {
            tvAchievementName.setText(achievement.getName());
            tvAchievementDescription.setText(achievement.getDescription());

            // Format and set the date
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            tvAchievementDate.setText(dateFormat.format(achievement.getDate()));

            // Set achievement icon based on type
            // In a real implementation, you might have different icons for different achievement types
            setAchievementIcon(achievement.getName());
        }

        private void setAchievementIcon(String achievementName) {
            // Set different icons based on achievement type
            if (achievementName.contains("Perfect Form")) {
                ivAchievementIcon.setImageResource(R.drawable.ic_star);
            } else if (achievementName.contains("Streak")) {
                ivAchievementIcon.setImageResource(R.drawable.ic_trophy);
            } else if (achievementName.contains("Calories")) {
                ivAchievementIcon.setImageResource(R.drawable.ic_fire);
            } else if (achievementName.contains("Exercise")) {
                ivAchievementIcon.setImageResource(R.drawable.ic_exercise);
            } else {
                // Default icon
                ivAchievementIcon.setImageResource(R.drawable.ic_achievement);
            }
        }
    }
}