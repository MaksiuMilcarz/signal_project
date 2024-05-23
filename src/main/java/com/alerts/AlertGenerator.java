package com.alerts;

import com.dataManagement.DataStorage;
import com.dataManagement.Patient;

import java.util.ArrayList;
import java.util.List;

public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;
    private List<AlertStrategy> alertStrategies;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
        this.alertStrategies = new ArrayList<>();
        initializeAlertStrategies();
    }

    private void initializeAlertStrategies() {
        this.alertStrategies.add(new BloodPressureStrategy());
        this.alertStrategies.add(new HypoxiaAlertStrategy());
        this.alertStrategies.add(new HeartRateStrategy());
        this.alertStrategies.add(new OxygenSaturationStrategy());
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void evaluateData(Patient patient) {
        for (AlertStrategy alertStrategy : alertStrategies) {
            Alert alert = alertStrategy.checkAlert(patient);
            if (alert != null) {
                triggerAlert(alert);
            }
        }
    }

    private void triggerAlert(Alert alert) {
        alerts.add(alert);
        System.out.println("ALERT: " + alert.getCondition());
    }
}
