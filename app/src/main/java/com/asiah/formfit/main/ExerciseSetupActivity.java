package com.asiah.formfit.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * ExerciseSetupActivity handles camera preview and prepares for exercise recording.
 * It allows users to position themselves correctly before starting the exercise.
 */
public class ExerciseSetupActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST = 100;

    private FrameLayout cameraPreviewContainer;
    private Button btnStartExercise;
    private ImageButton btnHome, btnExercises, btnProgress, btnSettings, btnMenu;

    // Camera handling components would be initialized here in a complete implementation
    // private CameraSource cameraSource;
    // private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_setup);

        // Initialize UI components
        cameraPreviewContainer = findViewById(R.id.cameraPreviewContainer);
        btnStartExercise = findViewById(R.id.btnStartExercise);
        btnHome = findViewById(R.id.btnHome);
        btnExercises = findViewById(R.id.btnExercises);
        btnProgress = findViewById(R.id.btnProgress);
        btnSettings = findViewById(R.id.btnSettings);
        btnMenu = findViewById(R.id.btnMenu);

        // Check for camera permission
        if (hasCameraPermission()) {
            setupCamera();
        } else {
            requestCameraPermission();
        }

        // Set up click listeners
        btnStartExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExerciseTracking();
            }
        });

        btnExercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToExerciseLibrary();
            }
        });

        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProgress();
            }
        });
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupCamera();
            } else {
                Toast.makeText(this, "Camera permission is required for exercise analysis", Toast.LENGTH_LONG).show();
                finish(); // Close activity if permission is denied
            }
        }
    }

    private void setupCamera() {
        // In a complete implementation, this would initialize the camera
        // and set up pose detection. For prototype purposes, we'll simulate this.

        Toast.makeText(this, "Camera initialized", Toast.LENGTH_SHORT).show();

        // This would normally be where you:
        // 1. Initialize the camera source with pose detection
        // 2. Create a camera preview surface
        // 3. Attach it to the container

        /*
        // Example code for actual implementation:
        cameraSource = new CameraSource.Builder(this, poseDetector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedPreviewSize(640, 480)
                .setRequestedFps(30.0f)
                .build();

        cameraPreview = new CameraPreview(this, cameraSource);
        cameraPreviewContainer.addView(cameraPreview);
        */
    }

    private void startExerciseTracking() {
        // In a complete implementation, this would start the TensorFlow model
        // and transition to the active exercise screen

        // For prototype, we'll just navigate to the next screen
        Intent intent = new Intent(ExerciseSetupActivity.this, ActiveExerciseActivity.class);
        // You could pass selected exercise info as extras here
        intent.putExtra("EXERCISE_NAME", "Squat");
        startActivity(intent);
    }

    private void navigateToExerciseLibrary() {
        Intent intent = new Intent(ExerciseSetupActivity.this, ExerciseLibraryActivity.class);
        startActivity(intent);
    }

    private void navigateToProgress() {
        Intent intent = new Intent(ExerciseSetupActivity.this, ProgressActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // In a complete implementation, you would start the camera here
        // if (hasCameraPermission() && cameraPreview != null) {
        //     try {
        //         cameraPreview.start();
        //     } catch (IOException e) {
        //         Log.e("CAMERA", "Could not start camera", e);
        //     }
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // In a complete implementation, you would stop the camera here
        // if (cameraPreview != null) {
        //     cameraPreview.stop();
        // }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // In a complete implementation, you would release resources here
        // if (cameraSource != null) {
        //     cameraSource.release();
        //     cameraSource = null;
        // }
    }
}
