package cardioGenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.cardioGenerator.HealthDataSimulator;

public class HealthDataSimulatorTest {
    @Test
    public void testInitializePatientIds() {
        List<Integer> patientIds = HealthDataSimulator.initializePatientIds(50);
        assertEquals(50, patientIds.size());
    }
}
