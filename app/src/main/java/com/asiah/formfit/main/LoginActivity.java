package com.asiah.formfit.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.asiah.formfit.R;

/**
 * LoginActivity handles user authentication
 * Simplified version for prototype without AppCompat
 */
public class LoginActivity extends Activity {

    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private Button btnGoogleLogin;
    private Button btnFacebookLogin;

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
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Simple validation for prototype
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // For prototype, simulate successful sign up
        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show();
        navigateToExerciseSetup();
    }

    private void performSocialLogin(String provider) {
        // For prototype, simulate successful social login
        Toast.makeText(this, "Signing in with " + provider + "...", Toast.LENGTH_SHORT).show();
        navigateToExerciseSetup();
    }

    private void navigateToExerciseSetup() {
        try {
            Intent intent = new Intent(LoginActivity.this, ExerciseSetupActivity.class);
            startActivity(intent);
            finish(); // Close login activity so user can't go back
        } catch (Exception e) {
            // If ExerciseSetupActivity doesn't exist yet, just show a message
            Toast.makeText(this, "Exercise Setup coming soon!", Toast.LENGTH_SHORT).show();
        }
    }
}