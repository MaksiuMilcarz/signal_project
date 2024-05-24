

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.AlertGenerator;
import com.dataManagement.DataStorage;
import com.dataManagement.Patient;

import java.io.IOException;

class AlertGenerationTest {
    @Test
    void testNoAlerts() throws IOException {
        DataStorage storage = DataStorage.getInstance();
        Patient patient = new Patient(1);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789010L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789052L);
        patient.addRecord(98.0, "Saturation", 1714376789053L);
        patient.addRecord(60.0, "ECG", 1714376789054L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789045L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789046L);
        patient.addRecord(98.0, "Saturation", 1714376789047L);
        patient.addRecord(60.0, "ECG", 1714376789048L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789120L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789140L);
        patient.addRecord(98.0, "Saturation", 1714376789240L);
        patient.addRecord(60.0, "ECG", 1714376789340L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789439L);
        patient.addRecord(110.0, "SystolicPressure", 1714376789539L);
        patient.addRecord(98.0, "Saturation", 1714376787039L);
        patient.addRecord(60.0, "ECG", 1714376784039L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertTrue(alertGenerator.getAlerts().isEmpty());
        DataStorage.removeInstance();
    }

    @Test
    void testAbnormalDataAlerts(){
        DataStorage storage = DataStorage.getInstance();
        Patient patient = new Patient(1);
        // Add records with abnormal values
        patient.addRecord(190.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(190.0, "SystolicPressure", 1714376789051L);
        patient.addRecord(80.0, "Saturation", 1714376789052L);
        patient.addRecord(20.0, "ECG", 1714376789053L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertEquals(3, alertGenerator.getAlerts().size());
        DataStorage.removeInstance();
    }

    @Test
    void testAbnormalTrendsInData(){
        DataStorage storage = DataStorage.getInstance();
        Patient patient = new Patient(1);
        // Add records with abnormal trends
        patient.addRecord(70.0, "DiastolicPressure", 1714376789050L);
        patient.addRecord(85.0, "DiastolicPressure", 1714376789051L);
        patient.addRecord(100.0, "DiastolicPressure", 1714376789052L);
        patient.addRecord(115.0, "DiastolicPressure", 1714376789053L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertEquals(1, alertGenerator.getAlerts().size());
        DataStorage.removeInstance();
    }

    @Test
    void testHypoxia(){
        Patient patient = new Patient(1);
        DataStorage storage = DataStorage.getInstance();
        patient.addRecord(40.0, "SystolicPressure", 1714376789050L);
        patient.addRecord(50.0, "Saturation", 1714376789051L);

        AlertGenerator alertGenerator = new AlertGenerator(storage);
        alertGenerator.evaluateData(patient);
        assertEquals(3, alertGenerator.getAlerts().size());
        DataStorage.removeInstance();
    }
}
