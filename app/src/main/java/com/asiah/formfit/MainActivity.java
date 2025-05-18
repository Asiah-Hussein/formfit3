package com.asiah.formfit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

import com.asiah.formfit.main.LoginActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create splash screen programmatically
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.parseColor("#3F51B5"));
        layout.setPadding(40, 80, 40, 40);
        layout.setGravity(Gravity.CENTER);

        // App title
        TextView titleText = new TextView(this);
        titleText.setText("FormFit");
        titleText.setTextSize(36);
        titleText.setTextColor(Color.WHITE);
        titleText.setGravity(Gravity.CENTER);
        titleText.setPadding(20, 60, 20, 20);
        layout.addView(titleText);

        // Subtitle
        TextView subtitleText = new TextView(this);
        subtitleText.setText("AI-Powered Exercise Form Analysis");
        subtitleText.setTextSize(18);
        subtitleText.setTextColor(Color.WHITE);
        subtitleText.setGravity(Gravity.CENTER);
        subtitleText.setPadding(20, 0, 20, 60);
        layout.addView(subtitleText);

        // Info
        TextView infoText = new TextView(this);
        infoText.setText("Module: CMP6213\nBy: Asiah Abdisalam Hussein\nID: 22131051\n\nLoading...");
        infoText.setTextColor(Color.WHITE);
        infoText.setTextSize(16);
        infoText.setGravity(Gravity.CENTER);
        infoText.setPadding(20, 20, 20, 20);
        layout.addView(infoText);

        setContentView(layout);

        // Navigate to LoginActivity after 3 seconds
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    // If LoginActivity fails, stay on main screen
                    TextView errorText = findViewById(android.R.id.text1);
                    if (errorText != null) {
                        errorText.setText("LoginActivity not available yet");
                    }
                }
            }
        }, 3000);
    }
}