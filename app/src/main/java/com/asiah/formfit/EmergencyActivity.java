package com.asiah.formfit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.main.LoginActivity;

public class EmergencyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Try to launch the login activity directly
        try {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close this activity
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // If that fails, set up a simple UI with a button to try again
        TextView tvAppName = findViewById(R.id.tvAppName);
        TextView tvAppTagline = findViewById(R.id.tvAppTagline);

        if (tvAppName != null && tvAppTagline != null) {
            tvAppName.setText("FormFit");
            tvAppTagline.setText("Some components couldn't be loaded.\nTap to try again.");

            View.OnClickListener retryListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(EmergencyActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(EmergencyActivity.this, "Still unable to launch. Please check errors.", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            tvAppName.setOnClickListener(retryListener);
            tvAppTagline.setOnClickListener(retryListener);
        }
    }
}