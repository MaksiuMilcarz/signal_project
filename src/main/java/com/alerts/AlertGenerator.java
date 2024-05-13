package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.dataManagement.DataStorage;
import com.dataManagement.Patient;
import com.dataManagement.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private List<Alert> alerts;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.alerts = new ArrayList<>();
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        if (patient == null) {
            System.out.println("ERROR: Patient data is null.");
            return;
        }
        List<PatientRecord> records = patient.getRecords(1700000000000L,1800000000000L);
        if (records == null || records.isEmpty()) {
            System.out.println("No records found for patient " + patient.getPatientId());
            return;
        }

        //make a seperate list of each type of record
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

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
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
            triggerAlert(new Alert(patient.getPatientId(), "Hypoxia", latestSystolicPressureRecord.getTimestamp()));
        }
    }

    private void evaluateDiastolicPressure(Patient patient, List<PatientRecord> diastolicPressureRecords) {
        int count = 0;
        for (int i = diastolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = diastolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 120 || currentRecord.getMeasurementValue() < 60) {
                triggerAlert(new Alert(patient.getPatientId(), "abnormalDiastolicPressure", currentRecord.getTimestamp()));
                break;
            } else {
                if (i > 0 && Math.abs(diastolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                triggerAlert(new Alert(patient.getPatientId(), "changingDiastolicPressure", currentRecord.getTimestamp()));
                break;
            }
        }
    }
    
    private void evaluateSystolicPressure(Patient patient, List<PatientRecord> systolicPressureRecords) {
        int count = 0;
        for (int i = systolicPressureRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = systolicPressureRecords.get(i);
            if (currentRecord.getMeasurementValue() > 180 || currentRecord.getMeasurementValue() < 90) {
                triggerAlert(new Alert(patient.getPatientId(), "abnormalSystolicPressure", currentRecord.getTimestamp()));
                break;
            } else {
                if (i > 0 && Math.abs(systolicPressureRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) > 10) {
                    count++;
                } else {
                    count = 0;
                }
            }
            if (count == 3) {
                triggerAlert(new Alert(patient.getPatientId(), "changingSystolicPressure", currentRecord.getTimestamp()));
                break;
            }
        }
    }
    
    private void evaluateBloodSaturation(Patient patient, List<PatientRecord> bloodSaturationRecords) {
        for (int i = bloodSaturationRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = bloodSaturationRecords.get(i);
            if (currentRecord.getMeasurementValue() < 92) {
                triggerAlert(new Alert(patient.getPatientId(), "abnormalBloodSaturation", currentRecord.getTimestamp()));
                break;
            } else {
                if (i > 0 && bloodSaturationRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue() > 5
                        && Math.abs(bloodSaturationRecords.get(i - 1).getTimestamp() - currentRecord.getTimestamp()) <= 10) {
                    triggerAlert(new Alert(patient.getPatientId(), "decreasingBloodSaturation", currentRecord.getTimestamp()));
                }
            }
        }
    }
    
    private void evaluateECG(Patient patient, List<PatientRecord> ECGRecords) {
        for (int i = ECGRecords.size() - 1; i >= 0; i--) {
            PatientRecord currentRecord = ECGRecords.get(i);
            if (currentRecord.getMeasurementValue() > 100 || currentRecord.getMeasurementValue() < 50) {
                triggerAlert(new Alert(patient.getPatientId(), "abnormalECG", currentRecord.getTimestamp()));
                break;
            } else {
                if (i > 0 && Math.abs(ECGRecords.get(i - 1).getMeasurementValue() - currentRecord.getMeasurementValue()) >= 10) {
                    triggerAlert(new Alert(patient.getPatientId(), "spikingECG", currentRecord.getTimestamp()));
                    break;
                }
            }
        }
    }
}
