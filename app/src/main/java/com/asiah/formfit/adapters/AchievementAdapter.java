// File: app/src/main/java/com/asiah/formfit/wearable/WearableController.java
package com.asiah.formfit.wearable;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;

import com.asiah.formfit.model.MotionPattern; // Fixed import
import com.asiah.formfit.utils.MotionAnalyzer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.CapabilityClient;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

/**
 * WearableController manages communication with wearable devices
 * for receiving sensor data and sending haptic feedback
 */
public class WearableController implements
        DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener,
        CapabilityClient.OnCapabilityChangedListener {

    private static final String TAG = "WearableController";

    // Path for sensor data
    private static final String SENSOR_DATA_PATH = "/sensor-data";
    // Path for haptic feedback
    private static final String HAPTIC_FEEDBACK_PATH = "/haptic-feedback";
    // Path for heart rate data
    private static final String HEART_RATE_PATH = "/heart-rate";

    // Capability names
    private static final String MOTION_SENSOR_CAPABILITY = "motion_sensor";
    private static final String HEART_RATE_CAPABILITY = "heart_rate_sensor";

    // Context
    private Context context;

    // Wearable clients
    private DataClient dataClient;
    private MessageClient messageClient;
    private CapabilityClient capabilityClient;

    // Connected nodes
    private List<Node> connectedNodes = new ArrayList<>();

    // Motion analyzer
    private MotionAnalyzer motionAnalyzer;

    // Vibrator for haptic feedback
    private Vibrator vibrator;

    // Listeners
    private SensorDataListener sensorDataListener;

    /**
     * Constructor initializes wearable connections
     */
    public WearableController(Context context) {
        this.context = context;

        // Initialize Wearable API clients
        dataClient = Wearable.getDataClient(context);
        messageClient = Wearable.getMessageClient(context);
        capabilityClient = Wearable.getCapabilityClient(context);

        // Initialize motion analyzer
        motionAnalyzer = new MotionAnalyzer();

        // Get vibrator service for haptic feedback
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        // Find connected wearable devices
        findConnectedNodes();
    }

    /**
     * Start listening for wearable data and events
     */
    public void startListening() {
        // Register listeners
        dataClient.addListener(this);
        messageClient.addListener(this);

        // Register capabilities we're interested in
        Task<Void> motionCapabilityTask = capabilityClient.addListener(
                this, MOTION_SENSOR_CAPABILITY);

        Task<Void> heartRateCapabilityTask = capabilityClient.addListener(
                this, HEART_RATE_CAPABILITY);

        Log.d(TAG, "Started listening for wearable data");
    }

    /**
     * Stop listening for wearable data and events
     */
    public void stopListening() {
        // Unregister listeners
        dataClient.removeListener(this);
        messageClient.removeListener(this);
        capabilityClient.removeListener(this);

        Log.d(TAG, "Stopped listening for wearable data");
    }

    /**
     * Find connected wearable devices
     */
    private void findConnectedNodes() {
        Wearable.getNodeClient(context).getConnectedNodes()
                .addOnSuccessListener(new OnSuccessListener<List<Node>>() {
                    @Override
                    public void onSuccess(List<Node> nodes) {
                        connectedNodes = nodes;
                        Log.d(TAG, "Found " + nodes.size() + " connected wearable nodes");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to get connected nodes: " + e.getMessage());
                    }
                });
    }

    /**
     * Send haptic feedback to wearable device
     * @param type Type of feedback (error, notification, etc.)
     */
    public void sendHapticFeedback(int type) {
        // For local testing, we can use the phone's vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            // Create vibration pattern based on feedback type
            long[] pattern;
            int amplitude;

            switch (type) {
                case 0: // Error
                    pattern = new long[]{0, 500, 200, 500};
                    amplitude = 255; // Max amplitude
                    break;
                case 1: // Warning
                    pattern = new long[]{0, 300, 200, 300};
                    amplitude = 200;
                    break;
                case 2: // Success
                    pattern = new long[]{0, 150, 100, 150, 100, 150};
                    amplitude = 150;
                    break;
                default: // Notification
                    pattern = new long[]{0, 250};
                    amplitude = 150;
                    break;
            }

            // Create vibration effect
            VibrationEffect effect = VibrationEffect.createWaveform(pattern, -1);

            // Vibrate
            vibrator.vibrate(effect);
        }

        // Send feedback to wearable devices
        if (!connectedNodes.isEmpty()) {
            // Create data map with feedback type
            DataMap dataMap = new DataMap();
            dataMap.putInt("feedback_type", type);

            // Send to all connected nodes
            for (Node node : connectedNodes) {
                messageClient.sendMessage(node.getId(), HAPTIC_FEEDBACK_PATH, dataMap.toByteArray())
                        .addOnSuccessListener(new OnSuccessListener<Integer>() {
                            @Override
                            public void onSuccess(Integer messageId) {
                                Log.d(TAG, "Haptic feedback sent to wearable: " + node.getDisplayName());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Failed to send haptic feedback: " + e.getMessage());
                            }
                        });
            }
        }
    }

    /**
     * Set a listener for sensor data events
     */
    public void setSensorDataListener(SensorDataListener listener) {
        this.sensorDataListener = listener;
    }

    /**
     * Process sensor data received from wearable
     */
    private void processSensorData(DataMap dataMap) {
        // Extract sensor data
        float[] accelerationData = dataMap.getFloatArray("acceleration");
        float[] gyroscopeData = dataMap.getFloatArray("gyroscope");
        float heartRate = dataMap.getFloat("heart_rate", 0);
        int exerciseType = dataMap.getInt("exercise_type", 0);

        // Process motion data
        if (accelerationData != null && gyroscopeData != null) {
            MotionPattern pattern = motionAnalyzer.detectPattern(accelerationData, gyroscopeData, exerciseType);

            // Check if pattern indicates incorrect form
            if (pattern.getType() == MotionPattern.TYPE_INCORRECT) {
                // Send haptic feedback for form correction
                sendHapticFeedback(1); // Warning type
            }

            // Notify listener
            if (sensorDataListener != null) {
                sensorDataListener.onMotionDataReceived(pattern);
                sensorDataListener.onHeartRateReceived(heartRate);
            }
        }
    }

    // DataClient.OnDataChangedListener implementation
    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();

                if (item.getUri().getPath().equals(SENSOR_DATA_PATH)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    processSensorData(dataMap);
                } else if (item.getUri().getPath().equals(HEART_RATE_PATH)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    float heartRate = dataMap.getFloat("heart_rate", 0);

                    // Notify listener
                    if (sensorDataListener != null) {
                        sensorDataListener.onHeartRateReceived(heartRate);
                    }
                }
            }
        }
    }

    // MessageClient.OnMessageReceivedListener implementation
    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        String path = messageEvent.getPath();

        if (path.equals(SENSOR_DATA_PATH)) {
            DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
            processSensorData(dataMap);
        } else if (path.equals(HEART_RATE_PATH)) {
            DataMap dataMap = DataMap.fromByteArray(messageEvent.getData());
            float heartRate = dataMap.getFloat("heart_rate", 0);

            // Notify listener
            if (sensorDataListener != null) {
                sensorDataListener.onHeartRateReceived(heartRate);
            }
        }
    }

    // CapabilityClient.OnCapabilityChangedListener implementation
    @Override
    public void onCapabilityChanged(@NonNull CapabilityInfo capabilityInfo) {
        // Update connected nodes based on capability changes
        connectedNodes = new ArrayList<>(capabilityInfo.getNodes());
        Log.d(TAG, "Capability changed: " + capabilityInfo.getName() +
                " with " + connectedNodes.size() + " nodes");
    }

    /**
     * Simulate sensor data for testing when no wearable is connected
     */
    public void simulateSensorData(int exerciseType) {
        // Create simulated acceleration and gyroscope data
        float[] accelerationData = new float[3];
        float[] gyroscopeData = new float[3];

        // Simulate typical values during exercise
        accelerationData[0] = (float) (Math.random() * 2 - 1); // X: -1 to 1
        accelerationData[1] = (float) (Math.random() * 4 - 2); // Y: -2 to 2
        accelerationData[2] = 9.8f + (float) (Math.random() * 2 - 1); // Z: ~9.8 (gravity) with noise

        gyroscopeData[0] = (float) (Math.random() * 2 - 1); // X rotation
        gyroscopeData[1] = (float) (Math.random() * 2 - 1); // Y rotation
        gyroscopeData[2] = (float) (Math.random() * 0.5 - 0.25); // Z rotation

        // Simulate heart rate (60-180 BPM)
        float heartRate = 60 + (float) (Math.random() * 120);

        // Create motion pattern
        MotionPattern pattern = motionAnalyzer.detectPattern(accelerationData, gyroscopeData, exerciseType);

        // Notify listener
        if (sensorDataListener != null) {
            sensorDataListener.onMotionDataReceived(pattern);
            sensorDataListener.onHeartRateReceived(heartRate);
        }
    }
}