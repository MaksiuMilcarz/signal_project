package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.dataManagement.DataStorage;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void evaluateData(Patient patient) {
        if (patient == null) {
            System.out.println("ERROR: Patient data is null.");
            return;
        }
        List<PatientRecord> records = patient.getRecords(1700000000000L, 1800000000000L);
        if (records == null || records.isEmpty()) {
            System.out.println("No records found for patient " + patient.getPatientId());
            return;
        }

        List<PatientRecord> diastolicPressureRecords = new ArrayList<>();
        List<PatientRecord> systolicPressureRecords = new ArrayList<>();
        List<PatientRecord> bloodSaturationRecords = new ArrayList<>();
        List<PatientRecord> ECGRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            String recordType = record.getRecordType();
            switch (recordType) {
                case "DiastolicPressure":
                    diastolicPressureRecords.add(record);
                    break;
                case "SystolicPressure":
                    systolicPressureRecords.add(record);
                    break;
                case "Saturation":
                    bloodSaturationRecords.add(record);
                    break;
                case "ECG":
                    ECGRecords.add(record);
                    break;
                default:
                    System.out.println("Unknown record type: " + recordType);
                    break;
            }
        }

        evaluateHypoxia(patient, systolicPressureRecords, bloodSaturationRecords);
        evaluateDiastolicPressure(patient, diastolicPressureRecords);
        evaluateSystolicPressure(patient, systolicPressureRecords);
        evaluateBloodSaturation(patient, bloodSaturationRecords);
        evaluateECG(patient, ECGRecords);
    }

    private void triggerAlert(Alert alert) {
        alerts.add(alert);
        System.out.println("ALERT: " + alert.toString());
    }

    private void evaluateHypoxia(Patient patient, List<PatientRecord> systolicPressureRecords, List<PatientRecord> bloodSaturationRecords) {
        if (systolicPressureRecords.isEmpty() || bloodSaturationRecords.isEmpty()) {
            return;
        }

        PatientRecord latestSystolicPressureRecord = systolicPressureRecords.get(systolicPressureRecords.size() - 1);
        PatientRecord latestBloodSaturationRecord = bloodSaturationRecords.get(bloodSaturationRecords.size() - 1);

        if (latestSystolicPressureRecord.getMeasurementValue() < 90 && latestBloodSaturationRecord.getMeasurementValue() <= 92) {
            AlertFactory factory = new BloodOxygenAlertFactory(patient.getPatientId(), latestSystolicPressureRecord.getTimestamp());
            triggerAlert(factory.createAlert("Hypoxia"));
        }
    }

    private void evaluateDiastolicPressure(Patient patient, List<PatientRecord> diastolicPressureRecords) {
        int count = 0;
        for (int i = diastolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = diastolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 120 || currentRecord.getMeasurementValue() < 60) {
                AlertFactory factory = new BloodPressureAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("abnormalDiastolicPressure"));
                break;
            } else {
                if (i > 0 && Math.abs(diastolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                AlertFactory factory = new BloodPressureAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("changingDiastolicPressure"));
                break;
            }
        }
    }

    private void evaluateSystolicPressure(Patient patient, List<PatientRecord> systolicPressureRecords) {
        int count = 0;
        for (int i = systolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = systolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 180 || currentRecord.getMeasurementValue() < 90) {
                AlertFactory factory = new BloodPressureAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("abnormalSystolicPressure"));
                break;
            } else {
                if (i > 0 && Math.abs(systolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                AlertFactory factory = new BloodPressureAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("changingSystolicPressure"));
                break;
            }
        }
    }

    private void evaluateBloodSaturation(Patient patient, List<PatientRecord> bloodSaturationRecords) {
        for (int i = bloodSaturationRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = bloodSaturationRecords.get(i);
            if (currentRecord.getMeasurementValue() < 92) {
                AlertFactory factory = new BloodOxygenAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("abnormalBloodSaturation"));
                break;
            } else {
                if (i > 0 && bloodSaturationRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue() > 5
                        && Math.abs(bloodSaturationRecords.get(i - 1).getTimestamp() - currentRecord.getTimestamp()) <= 10) {
                    AlertFactory factory = new BloodOxygenAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                    triggerAlert(factory.createAlert("decreasingBloodSaturation"));
                }
            }
        }
    }

    private void evaluateECG(Patient patient, List<PatientRecord> ECGRecords) {
        for (int i = ECGRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = ECGRecords.get(i);
            if (currentRecord.getMeasurementValue() > 100 || currentRecord.getMeasurementValue() < 50) {
                AlertFactory factory = new ECGAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                triggerAlert(factory.createAlert("abnormalECG"));
                break;
            } else {
                if (i > 0 && Math.abs(ECGRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) >= 10) {
                    AlertFactory factory = new ECGAlertFactory(patient.getPatientId(), currentRecord.getTimestamp());
                    triggerAlert(factory.createAlert("spikingECG"));
                    break;
                }
            }
        }
    }
}
