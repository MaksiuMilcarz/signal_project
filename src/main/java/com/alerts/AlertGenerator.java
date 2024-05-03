package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

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
        List<PatientRecord> records = patient.getRecords(1700000000000L,1800000000000L);

        //make a seperate list of each type of record
        List<PatientRecord> diastolicPressureRecords = new ArrayList<>();
        List<PatientRecord> systolicPressureRecords = new ArrayList<>();
        List<PatientRecord> bloodSaturationRecords = new ArrayList<>();
        List<PatientRecord> ECGRecords = new ArrayList<>();

        for (PatientRecord record : records) {
            if(record.getRecordType().equals("DiastolicPressure")){
                diastolicPressureRecords.add(record);
            }
            else if(record.getRecordType().equals("SystolicPressure")){
                systolicPressureRecords.add(record);
            }
            else if(record.getRecordType().equals("Saturation")){
                bloodSaturationRecords.add(record);
            }
            else if(record.getRecordType().equals("ECG")){
                ECGRecords.add(record);
            }
        }

        if(systolicPressureRecords.get(systolicPressureRecords.size()-1).getMeasurementValue() <90 && bloodSaturationRecords.get(bloodSaturationRecords.size()-1).getMeasurementValue() <=92){
            triggerAlert(new Alert(patient.getPatientId(), "Hypoxia", systolicPressureRecords.get(systolicPressureRecords.size()-1).getTimestamp()));
            return;
        }

        //Assume last records are the most recent
        //diastolic pressure
        int count = 0;
        for(int i = diastolicPressureRecords.size()-1; i >= diastolicPressureRecords.size() - 4; i--){
            if(diastolicPressureRecords.get(i).getMeasurementValue() > 120 || diastolicPressureRecords.get(i).getMeasurementValue() < 60){
                triggerAlert(new Alert(patient.getPatientId(), "abnormalDiastolicPressure", diastolicPressureRecords.get(i).getTimestamp()));
                break;
            }
            else{
                if(Math.abs(diastolicPressureRecords.get(i-1).getMeasurementValue() - diastolicPressureRecords.get(i).getMeasurementValue()) > 10){
                    count++;
                }
                else{
                    count = 0;
                }
            }
            if(count == 3){
                triggerAlert(new Alert(patient.getPatientId(), "changingDiastolicPressure", diastolicPressureRecords.get(i).getTimestamp()));
                count = 0;
                break;
            }
        }

        //systolic pressure
        
        for(int i = systolicPressureRecords.size()-1; i >= systolicPressureRecords.size() - 4; i--){
            if(systolicPressureRecords.get(i).getMeasurementValue() > 180 || systolicPressureRecords.get(i).getMeasurementValue() < 90){
                triggerAlert(new Alert(patient.getPatientId(), "abnormalSystolicPressure", systolicPressureRecords.get(i).getTimestamp()));
                break;
            }
            else{
                if(Math.abs(systolicPressureRecords.get(i-1).getMeasurementValue() - systolicPressureRecords.get(i).getMeasurementValue()) > 10){
                    count++;
                }
                else{
                    count = 0;
                }
            }
            if(count == 3){
                triggerAlert(new Alert(patient.getPatientId(), "changingSystolicPressure", systolicPressureRecords.get(i).getTimestamp()));
                count = 0;
                break;
            }
        }

        //bloood saturation
        for(int i = bloodSaturationRecords.size()-1; i>=bloodSaturationRecords.size()-4;i--){
            if(bloodSaturationRecords.get(i).getMeasurementValue() < 92){
                triggerAlert(new Alert(patient.getPatientId(), "abnormalBloodSaturation", bloodSaturationRecords.get(i).getTimestamp()));
                break;
            }
            else{
                if(bloodSaturationRecords.get(i-1).getMeasurementValue() - bloodSaturationRecords.get(i).getMeasurementValue() > 5 
                && Math.abs(bloodSaturationRecords.get(i-1).getTimestamp() - bloodSaturationRecords.get(i).getTimestamp()) <= 10){
                    triggerAlert(new Alert(patient.getPatientId(), "decreasingBloodSaturation", bloodSaturationRecords.get(i).getTimestamp()));
                }
            }
        }

        for(int i = ECGRecords.size()-1; i>=0;i--){
            if(ECGRecords.get(i).getMeasurementValue() > 100 || ECGRecords.get(i).getMeasurementValue() <50){
                triggerAlert(new Alert(patient.getPatientId(), "abnormalECG", ECGRecords.get(i).getTimestamp()));
                break;
            }
            else{
                if(Math.abs(ECGRecords.get(i-1).getMeasurementValue() - ECGRecords.get(i).getMeasurementValue()) >= 10){
                triggerAlert(new Alert(patient.getPatientId(), "spikingECG", ECGRecords.get(i).getTimestamp()));
                break;
                }
            }       
        }
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
}
