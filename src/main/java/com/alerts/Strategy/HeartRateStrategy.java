package com.alerts.Strategy;

import java.util.ArrayList;
import java.util.List;

import com.alerts.Alert;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

public class HeartRateStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(Patient patient) {
        if (patient == null) {
            System.out.println("ERROR: Patient data is null.");
            return null;
        }
        
        List<PatientRecord> records = patient.getRecords(0, 10000000000000L);
        if (records == null || records.isEmpty()) {
            return null;
        }

        List<PatientRecord> heartRateRecords = new ArrayList<>();
        
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("ECG")) {
                heartRateRecords.add(record);
            }
        }

        if (heartRateRecords.isEmpty()) {
            return null; 
        }

        for (PatientRecord record : heartRateRecords) {
            if (record.getMeasurementValue() > 100 || record.getMeasurementValue() < 50) {
                System.out.println("ALERT: abnormalHeartRate");
                return new Alert(patient.getPatientId(), "abnormalHeartRate", record.getTimestamp());
            }
        }
        return null; 
    }
}
