package com.alerts;

import com.dataManagement.Patient;

public interface AlertStrategy {
    Alert checkAlert(Patient patient);
}
