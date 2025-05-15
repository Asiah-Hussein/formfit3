// File: app/src/main/java/com/asiah/formfit/main/ProgressActivity.java
package com.asiah.formfit.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.R; // Correct import for R class
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * ProgressActivity displays the user's exercise history, achievements, and performance metrics.
 */
public class ProgressActivity extends AppCompatActivity {

    private TextView tvExerciseCount;
    private LineChart lineChart;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings;

    // In a complete implementation, these would be loaded from a database
    private int weeklyExerciseCount = 23; // Number of exercises this week
    private List<Float> weeklyFormAccuracy; // Daily form accuracy percentage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        // Initialize UI components
        tvExerciseCount = findViewById(R.id.tvExerciseCount);
        lineChart = findViewById(R.id.lineChart);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);

        // Set up exercise count
        tvExerciseCount.setText(String.valueOf(weeklyExerciseCount));

        // Generate sample data
        generateSampleData();

        // Set up progress chart
        setupProgressChart();

        // Set up navigation listeners
        setupNavigationListeners();
    }

    private void generateSampleData() {
        // In a real app, this would be loaded from a database
        // For prototype, generate a week of sample data
        weeklyFormAccuracy = new ArrayList<>();
        weeklyFormAccuracy.add(75f); // Day 1
        weeklyFormAccuracy.add(78f); // Day 2
        weeklyFormAccuracy.add(82f); // Day 3
        weeklyFormAccuracy.add(80f); // Day 4
        weeklyFormAccuracy.add(85f); // Day 5
        weeklyFormAccuracy.add(88f); // Day 6
        weeklyFormAccuracy.add(92f); // Day 7 (today)
    }

    private void setupProgressChart() {
        // Configure chart appearance
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.getLegend().setEnabled(false);

        // Configure X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // Set day labels
        final String[] days = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));

        // Configure Y axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(60f); // Start from 60% for better visualization
        leftAxis.setAxisMaximum(100f); // Max 100%
        leftAxis.setDrawGridLines(true);

        // Disable right Y axis
        lineChart.getAxisRight().setEnabled(false);

        // Create data entries
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < weeklyFormAccuracy.size(); i++) {
            entries.add(new Entry(i, weeklyFormAccuracy.get(i)));
        }

        // Create dataset
        LineDataSet dataSet = new LineDataSet(entries, "Form Accuracy");
        dataSet.setColor(Color.BLUE);
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleRadius(2f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#3498db"));
        dataSet.setFillAlpha(50);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Smooth curve

        // Create line data
        LineData lineData = new LineData(dataSet);

        // Set data to chart
        lineChart.setData(lineData);
        lineChart.animateX(1000); // Animate chart
        lineChart.invalidate(); // Refresh
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

        // Progress button is already active

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For prototype, we won't implement settings
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