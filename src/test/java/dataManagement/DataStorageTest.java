package dataManagement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.dataManagement.DataStorage;
import com.dataManagement.PatientRecord;

import java.io.IOException;
import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() throws IOException {
        // DataReader reader
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(1, 100.0, "WhiteBloodCells", 1714376789050L);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", 1714376789051L);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);
        assertEquals(2, records.size()); // Check if two records are retrieved
        assertEquals(100.0, records.get(0).getMeasurementValue()); // Validate first record
        DataStorage.removeInstance();
    }
}
