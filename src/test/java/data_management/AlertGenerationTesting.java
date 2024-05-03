package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.junit.*;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;

import java.io.IOException;

public class AlertGenerationTesting {
    @Test
    void testNoAlerts() throws IOException {
        DataStorage storage = new DataStorage();
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789050L);
        patient.addRecord(98.0, "Saturation", 1714376789050L);
        patient.addRecord(60.0, "ECG", 1714376789050L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
    }

    @Test
    void testAbnormalDataAlerts(){
        DataStorage storage = new DataStorage();
        Patient patient = new Patient(1);
        patient.addRecord(190.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(190.0, "SystolicPressure", 1714376789050L);
        patient.addRecord(90.0, "Saturation", 1714376789050L);
        patient.addRecord(20.0, "ECG", 1714376789050L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().size()==4);
    }

    @Test
    void testAbnormalTrendsInData(){
        DataStorage storage = new DataStorage();
        Patient patient = new Patient(1);
        patient.addRecord(70.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(85.0, "DiastolicPressure", 1714376789051L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789052L);
        patient.addRecord(115.0, "DiastolicPressure", 1714376789053L);
        patient.addRecord(95.0, "SystolicPressure", 1714376789054L);
        patient.addRecord(105.0, "SystolicPressure", 1714376789055L);
        patient.addRecord(115.0, "SystolicPressure", 1714376789056L);
        patient.addRecord(125.0, "SystolicPressure", 1714376789057L);
        patient.addRecord(100.0, "Saturation", 1714376789058L);
        patient.addRecord(94.0, "Saturation", 1714376789059L);
        patient.addRecord(60.0, "ECG", 1714376789060L);
        patient.addRecord(90.0, "ECG", 1714376789061L);
        patient.addRecord(70.0, "ECG", 1714376789062L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().size()==4);
    }

    @Test
    void testHypoxia(){
        Patient patient = new Patient(1);
        DataStorage storage = new DataStorage();
        patient.addRecord(85.0, "SystolicPressure", 1714376789050L);
        patient.addRecord(90.0, "Saturation", 1714376789051L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().size()==1);
    }
}
