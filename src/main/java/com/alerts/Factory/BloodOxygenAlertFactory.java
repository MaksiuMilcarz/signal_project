package com.alerts.Factory;

import com.alerts.Alert;

public class BloodOxygenAlertFactory extends AlertFactory {
    private String patientId;
    private long timestamp;

    public BloodOxygenAlertFactory(String patientId, long timestamp) {
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    @Override
    public Alert createAlert(String condition) {
        return new Alert(patientId, condition, timestamp);
    }
}
