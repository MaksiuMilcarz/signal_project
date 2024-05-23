package com.alerts.Strategy;

import com.alerts.Alert;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

import java.util.List;

public class HypoxiaAlertStrategy implements AlertStrategy {

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

        PatientRecord latestSystolicPressureRecord = null;
        PatientRecord latestBloodSaturationRecord = null;

        for (PatientRecord record : records) {
            if (record.getRecordType().equals("SystolicPressure") && (latestSystolicPressureRecord == null || record.getTimestamp() > latestSystolicPressureRecord.getTimestamp())) {
                latestSystolicPressureRecord = record;
            }
            if (record.getRecordType().equals("Saturation") && (latestBloodSaturationRecord == null || record.getTimestamp() > latestBloodSaturationRecord.getTimestamp())) {
                latestBloodSaturationRecord = record;
            }
        }
        System.out.println("latestSystolicPressureRecord: " + latestSystolicPressureRecord);
        System.out.println("latestBloodSaturationRecord: " + latestBloodSaturationRecord);

        if (latestSystolicPressureRecord != null && latestBloodSaturationRecord != null) {
            if (latestSystolicPressureRecord.getMeasurementValue() < 90 && latestBloodSaturationRecord.getMeasurementValue() <= 92) {
                System.out.println("ALERT: Hypoxia");
                return new Alert(patient.getPatientId(), "Hypoxia", latestSystolicPressureRecord.getTimestamp());
            }
        }

        return null;
    }
}
