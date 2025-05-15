// File: app/src/main/java/com/asiah/formfit/EmergencyActivity.java
package com.asiah.formfit;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Simple text view
        TextView tv = new TextView(this);
        tv.setPadding(50, 100, 50, 50);
        tv.setTextSize(18);
        tv.setText("FormFit: AI-Powered Exercise Form Analysis\n\n" +
                "By: Asiah Abdisalam Hussein\n\n" +
                "Module: CMP6213 Mobile and Wearable Application Development\n\n" +
                "This prototype demonstrates an application that uses AI to analyze exercise form and provide feedback to users in real-time.\n\n" +
                "The prototype implementation encountered technical issues with package structure and class references that could not be resolved before the submission deadline.");

        setContentView(tv);
    }
}
