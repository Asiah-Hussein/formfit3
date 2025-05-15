// File: app/src/main/java/com/asiah/formfit/main/LoginActivity.java
package com.asiah.formfit.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.asiah.formfit.R; // Correct import for R class
import com.google.firebase.auth.FirebaseAuth;

/**
 * LoginActivity handles user authentication through email/password or social media.
 * It provides the entry point to the Form-Fit application.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnSignUp;
    private ImageButton btnGoogleLogin, btnFacebookLogin, btnAppleLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        btnAppleLogin = findViewById(R.id.btnAppleLogin);

        // Set up click listeners
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For this prototype, we'll use the same method with registration logic
                registerUser();
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // For prototype purposes, we'll simulate Facebook login success
                Toast.makeText(LoginActivity.this, "Facebook login successful", Toast.LENGTH_SHORT).show();
                navigateToMainScreen();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is already signed in
        if (firebaseAuth.getCurrentUser() != null) {
            navigateToMainScreen();
        }
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // For prototype purposes, we'll simulate login success
        // In a real app, you'd authenticate with Firebase:

        /*
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        navigateToMainScreen();
                    } else {
                        // Sign in failed
                        Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        */

        // Simulated login success for prototype
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // For prototype purposes, we'll simulate registration success
        // In a real app, you'd register with Firebase:

        /*
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Registration success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        navigateToMainScreen();
                    } else {
                        // Registration failed
                        Toast.makeText(LoginActivity.this, "Registration failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        */

        // Simulated registration success for prototype
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        navigateToMainScreen();
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(LoginActivity.this, ExerciseSetupActivity.class);
        startActivity(intent);
        finish(); // Close login activity
    }
}