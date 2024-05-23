package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

import java.util.Collections;
import java.util.Comparator;

public class OxygenSaturationStrategy implements AlertStrategy {

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

        List<PatientRecord> bloodSaturationRecords = new ArrayList<>();
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("Saturation")) {
                bloodSaturationRecords.add(record);
            }
        }

        if (bloodSaturationRecords.isEmpty()) {
            return null; 
        }

        Collections.sort(bloodSaturationRecords, Comparator.comparingLong(PatientRecord::getTimestamp));

        // Check for low oxygen saturation
        for (PatientRecord record : bloodSaturationRecords) {
            if (record.getMeasurementValue() < 92) {
                System.out.println("ALERT: lowOxygenSaturation");
                return new Alert(patient.getPatientId(), "lowOxygenSaturation", record.getTimestamp());
            }
        }

        for (int i = 0; i < bloodSaturationRecords.size() - 1; i++) {
            PatientRecord currentRecord = bloodSaturationRecords.get(i);
            PatientRecord nextRecord = bloodSaturationRecords.get(i + 1);

            if (nextRecord == null || currentRecord == null) {
                continue;
            }

            long timeDifference = nextRecord.getTimestamp() - currentRecord.getTimestamp();
            double saturationDifference = nextRecord.getMeasurementValue() - currentRecord.getMeasurementValue();

            if (timeDifference <= 600000 && saturationDifference >= 5) {
                System.out.println("ALERT: rapidOxygenSaturationDrop");
                return new Alert(patient.getPatientId(), "rapidOxygenSaturationDrop", nextRecord.getTimestamp());
            }
        }

        return null; 
    }
}