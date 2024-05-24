package com.alerts;

import java.util.List;

public interface Alert {
    String getPatientId();
    String getCondition();
    long getTimestamp();
    void alertAction(List<Alert> alerts);
}