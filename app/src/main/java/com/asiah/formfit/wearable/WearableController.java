// Create WearableController.java interface in the com.asiah.formfit.wearable package
package com.asiah.formfit.wearable;

import com.asiah.formfit.model.MotionPattern;

/**
 * Interface for wearable device controllers
 */
public interface WearableController {
    void startListening();
    void stopListening();
    void sendHapticFeedback(int type);
    void setSensorDataListener(SensorDataListener listener);
    void simulateSensorData(int exerciseType);
}
