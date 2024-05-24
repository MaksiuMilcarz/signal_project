package dataManagement;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.junit.*;

import com.dataManagement.DataStorage;
import com.dataManagement.FileDataReader;
import com.dataManagement.PatientRecord;

import java.io.IOException;
import java.util.List;

class FileReaderTest {
    @Test
    void testReadingDataFromFiles() throws IOException {
        DataStorage storage = DataStorage.getInstance();
        FileDataReader reader = new FileDataReader();
        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(9, 1700000000000L, 1800000000000L);

        assertTrue(!records.isEmpty());
        DataStorage.removeInstance();
    }
}
