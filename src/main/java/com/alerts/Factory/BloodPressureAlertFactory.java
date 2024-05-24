package com.alerts.Factory;

import com.alerts.Alert;
import com.alerts.AlertBasic;

public class BloodPressureAlertFactory extends AlertFactory {
    private String patientId;
    private long timestamp;

    public BloodPressureAlertFactory(String patientId, long timestamp) {
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    @Override
    public Alert createAlert(String condition) {
        Alert alert = new AlertBasic(patientId, condition, timestamp);
        return alert;
    }
}