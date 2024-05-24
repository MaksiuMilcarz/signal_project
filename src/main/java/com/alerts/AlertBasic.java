package com.alerts;

import java.util.List;

// Represents an alert
public class AlertBasic implements Alert{
    private String patientId;
    private String condition;
    private long timestamp;

    public AlertBasic(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    public void alertAction(List<Alert> alerts){
        alerts.add(this);
    }
}
