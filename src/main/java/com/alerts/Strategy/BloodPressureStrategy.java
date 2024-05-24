package com.alerts.Strategy;

import com.alerts.Alert;
import com.alerts.AlertBasic;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

import java.util.ArrayList;
import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

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

        List<PatientRecord> diastolicPressureRecords = new ArrayList<>();
        List<PatientRecord> systolicPressureRecords = new ArrayList<>();
        for (PatientRecord record : records) {
            if (record.getRecordType().equals("DiastolicPressure")) {
                diastolicPressureRecords.add(record);
            }
            if (record.getRecordType().equals("SystolicPressure")) {
                systolicPressureRecords.add(record);
            }
        }

        Alert diastolicAlert = evaluateDiastolicPressure(patient, diastolicPressureRecords);
        if (diastolicAlert != null) {
            return diastolicAlert;
        }

        Alert systolicAlert = evaluateSystolicPressure(patient, systolicPressureRecords);
        if (systolicAlert != null) {
            return systolicAlert;
        }

        return null;
    }

    private Alert evaluateDiastolicPressure(Patient patient, List<PatientRecord> diastolicPressureRecords) {
        int count = 0;
        for (int i = diastolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = diastolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 120 || currentRecord.getMeasurementValue() < 60) {
                Alert alert = new AlertBasic(patient.getPatientId(), "abnormalDiastolicPressure", currentRecord.getTimestamp());
                return alert;
            } else {
                if (i > 0 && Math.abs(diastolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                Alert alert  =new AlertBasic(patient.getPatientId(), "changingDiastolicPressure", currentRecord.getTimestamp());
                return alert;
            }
        }
        return null;
    }

    private Alert evaluateSystolicPressure(Patient patient, List<PatientRecord> systolicPressureRecords) {
        int count = 0;
        for (int i = systolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = systolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 180 || currentRecord.getMeasurementValue() < 90) {
                Alert alert = new AlertBasic(patient.getPatientId(), "abnormalSystolicPressure", currentRecord.getTimestamp());
                return alert;
            } else {
                if (i > 0 && Math.abs(systolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                Alert alert = new AlertBasic(patient.getPatientId(), "changingSystolicPressure", currentRecord.getTimestamp());
                return alert;
            }
        }
        return null;
    }
}