package com.alerts;

public class BloodPressureAlertFactory extends AlertFactory {
    private String patientId;
    private long timestamp;

    public BloodPressureAlertFactory(String patientId, long timestamp) {
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    @Override
    public Alert createAlert(String condition) {
        return new Alert(patientId, condition, timestamp);
    }
}