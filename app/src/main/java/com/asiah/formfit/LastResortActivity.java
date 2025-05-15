package com.asiah.formfit;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LastResortActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a layout programmatically - no XML dependencies
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#3F51B5")); // Blue background
        layout.setPadding(40, 80, 40, 40);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        // Add title text
        TextView titleText = new TextView(this);
        titleText.setText("FormFit");
        titleText.setTextSize(32);
        titleText.setTextColor(Color.WHITE);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(20, 60, 20, 20);
        layout.addView(titleText);

        // Add subtitle
        TextView subtitleText = new TextView(this);
        subtitleText.setText("AI-Powered Exercise Form Analysis");
        subtitleText.setTextSize(18);
        subtitleText.setTextColor(Color.WHITE);
        subtitleText.setGravity(Gravity.CENTER);
        subtitleText.setPadding(20, 0, 20, 60);
        layout.addView(subtitleText);

        // Add description
        TextView descText = new TextView(this);
        descText.setText("Module: CMP6213\nBy: Asiah Abdisalam Hussein\nID: 22131051\n\n" +
                "This application combines mobile and wearable technology to analyze exercise form in real-time. It provides immediate feedback through computer vision analysis and haptic feedback from wearable sensors.\n\n" +
                "Key Features:\n• Real-time form analysis\n• Haptic feedback\n• Progress tracking\n• Exercise library");
        descText.setTextColor(Color.WHITE);
        descText.setTextSize(16);
        descText.setPadding(20, 20, 20, 20);
        layout.addView(descText);

        // Set content view
        setContentView(layout);
    }
}
