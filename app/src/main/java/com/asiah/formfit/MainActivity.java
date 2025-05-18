package com.asiah.formfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.main.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Log to verify MainActivity is starting
        Log.d(TAG, "MainActivity onCreate() called");

        try {
            // Apply theme and set content view
            setTheme(R.style.AppTheme);
            setContentView(R.layout.activity_main);

            Log.d(TAG, "MainActivity layout set successfully");

            // Add a delay and then navigate to the login screen
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigateToLogin();
                }
            }, 2000); // 2 second delay

        } catch (Exception e) {
            Log.e(TAG, "Error in MainActivity onCreate: " + e.getMessage(), e);
            // If there's an error, try to launch the fallback activity
            launchFallback();
        }
    }

    private void navigateToLogin() {
        try {
            Log.d(TAG, "Attempting to navigate to LoginActivity");

            // Create intent for LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);

            // Start the activity
            startActivity(intent);

            // Close this activity
            finish();

            Log.d(TAG, "Successfully navigated to LoginActivity");

        } catch (Exception e) {
            Log.e(TAG, "Error navigating to LoginActivity: " + e.getMessage(), e);
            // If LoginActivity fails, try the fallback
            launchFallback();
        }
    }

    private void launchFallback() {
        try {
            Log.d(TAG, "Launching fallback activity");
            Intent intent = new Intent(this, LastResortActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "Even fallback failed: " + e.getMessage(), e);
            // At this point, we'll just stay on the main activity
        }
    }
}