package cardioGenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

import com.cardioGenerator.outputs.ConsoleOutputStrategy;

public class outputsTest {
    @Test
    void testConsoleOutputStrategy() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        ConsoleOutputStrategy outputStrategy = new ConsoleOutputStrategy();
        int patientId = 1;
        long timestamp = System.currentTimeMillis();
        String label = "ECG";
        String data = "120";

        outputStrategy.output(patientId, timestamp, label, data);

        // Capture the output
        String expectedOutput = String.format("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                patientId, timestamp, label, data);
        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(originalOut); 
    }
}
