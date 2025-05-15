package com.asiah.formfit.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asiah.formfit.R;
import com.asiah.formfit.data.Achievement;

import java.util.List;

/**
 * Adapter for displaying achievements in a RecyclerView
 */
public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievements;
    private OnAchievementClickListener listener;

    /**
     * Interface for achievement item click events
     */
    public interface OnAchievementClickListener {
        void onAchievementClick(Achievement achievement);
    }

    public AchievementAdapter(List<Achievement> achievements, OnAchievementClickListener listener) {
        this.achievements = achievements;
        this.listener = listener;
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
        holder.bind(achievement, listener);
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
        private ImageView ivAchievementIcon;
        private TextView tvAchievementName;
        private TextView tvAchievementDescription;
        private TextView tvAchievementDate;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAchievementIcon = itemView.findViewById(R.id.ivAchievementIcon);
            tvAchievementName = itemView.findViewById(R.id.tvAchievementName);
            tvAchievementDescription = itemView.findViewById(R.id.tvAchievementDescription);
            tvAchievementDate = itemView.findViewById(R.id.tvAchievementDate);
        }

        public void bind(final Achievement achievement, final OnAchievementClickListener listener) {
            tvAchievementName.setText(achievement.getName());
            tvAchievementDescription.setText(achievement.getDescription());

            // Format date
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault());
            tvAchievementDate.setText(dateFormat.format(achievement.getDate()));

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAchievementClick(achievement);
                }
            });
        }
    }
}