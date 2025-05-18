package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.R;

/**
 * LoginActivity handles user authentication
 * Simplified version for prototype without Firebase
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private ImageButton btnGoogleLogin;
    private ImageButton btnFacebookLogin;
    private ImageButton btnAppleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        btnAppleLogin = findViewById(R.id.btnAppleLogin);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSignUp();
            }
        });

        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSocialLogin("Google");
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSocialLogin("Facebook");
            }
        });

        btnAppleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSocialLogin("Apple");
            }
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation for prototype
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        // For prototype, any email/password combination works
        if (email.length() > 0 && password.length() > 0) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            navigateToExerciseSetup();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void performSignUp() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return;
        }

        // For prototype, simulate successful sign up
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
        navigateToExerciseSetup();
    }

    private void performSocialLogin(String provider) {
        // For prototype, simulate successful social login
        Toast.makeText(this, "Signing in with " + provider + "...", Toast.LENGTH_SHORT).show();

        // Simulate delay for social login
        etEmail.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                navigateToExerciseSetup();
            }
        }, 1500);
    }

    private void navigateToExerciseSetup() {
        Intent intent = new Intent(LoginActivity.this, ExerciseSetupActivity.class);
        startActivity(intent);
        finish(); // Close login activity so user can't go back
    }
}