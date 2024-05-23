package com.alerts;

public class ECGAlertFactory extends AlertFactory {
    private String patientId;
    private long timestamp;

    public ECGAlertFactory(String patientId, long timestamp) {
        this.patientId = patientId;
        this.timestamp = timestamp;
    }

    @Override
    public Alert createAlert(String condition) {
        return new Alert(patientId, condition, timestamp);
    }
}
