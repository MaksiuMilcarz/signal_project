package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.junit.*;

import com.data_management.DataStorage;
import com.data_management.FileDataReader;
import com.data_management.PatientRecord;

import java.io.IOException;
import java.util.List;

public class FilereaderTestting {
    @Test
    void testReadingDataFromFiles() throws IOException {
        FileDataReader reader = new FileDataReader();
        DataStorage storage = new DataStorage();
        reader.readData(storage);

        List<PatientRecord> records = storage.getRecords(1, 1714376789050L, 1714376789051L);

        assertTrue(!records.isEmpty());
    }
}
