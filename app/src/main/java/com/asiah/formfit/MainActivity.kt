package com.asiah.formfit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Log to verify MainActivity is launching
        Log.d(TAG, "MainActivity created")

        // Add a delay and then navigate to the login screen
        Handler(Looper.getMainLooper()).postDelayed({
            try {
                // Make sure you're using the correct package path for LoginActivity
                val intent = Intent(this, com.asiah.formfit.main.LoginActivity::class.java)

                // Log before starting activity
                Log.d(TAG, "Navigating to LoginActivity")
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                // Log any errors during navigation
                Log.e(TAG, "Error navigating to LoginActivity: ${e.message}")
                e.printStackTrace()
            }
        }, 2000) // 2 second delay
    }
}