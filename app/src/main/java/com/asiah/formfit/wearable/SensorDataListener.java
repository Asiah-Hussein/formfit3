// File: app/src/main/java/com/asiah/formfit/wearable/SensorDataListener.java
package com.asiah.formfit.wearable;

import com.asiah.formfit.model.MotionPattern;

/**
 * Interface for receiving sensor data from wearable devices
 */
public interface SensorDataListener {

    /**
     * Called when motion data is received from wearable
     * @param motionPattern The detected motion pattern
     */
    void onMotionDataReceived(MotionPattern motionPattern);

    /**
     * Called when heart rate data is received from wearable
     * @param heartRate Heart rate in beats per minute (BPM)
     */
    void onHeartRateReceived(float heartRate);

    /**
     * Called when an error occurs with the wearable connection
     * @param errorMessage Error message
     */
    void onWearableError(String errorMessage);
}
