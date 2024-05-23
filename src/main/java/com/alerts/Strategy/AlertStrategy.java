package com.alerts.Strategy;

import com.alerts.Alert;
import com.dataManagement.Patient;

public interface AlertStrategy {
    Alert checkAlert(Patient patient);
}
