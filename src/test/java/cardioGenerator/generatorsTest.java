package cardioGenerator;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.cardioGenerator.HealthDataSimulator;
import com.cardioGenerator.generators.BloodLevelsDataGenerator;
import com.cardioGenerator.generators.BloodPressureDataGenerator;
import com.cardioGenerator.generators.BloodSaturationDataGenerator;
import com.cardioGenerator.generators.ECGDataGenerator;
import com.cardioGenerator.outputs.ConsoleOutputStrategy;

public class generatorsTest {
    @Test
    void testGenerateECG() {
        ECGDataGenerator generator = new ECGDataGenerator(1);
        assertTrue(generator.getLastEcgValues().length == 2);
        assertTrue(generator.getLastEcgValues()[0] == 0);
        generator.generate(1, new ConsoleOutputStrategy());
        assertTrue(generator.getLastEcgValues()[1] != 0);
    }

    @Test
    void testBloodSaturationDataGenerator(){
        BloodSaturationDataGenerator generator = new BloodSaturationDataGenerator(2);
        assertTrue(generator.getLastSaturationValues().length == 3);
        assertTrue(generator.getLastSaturationValues()[1] > 94);
        assertTrue(generator.getLastSaturationValues()[1] < 101);
        assertTrue(generator.getLastSaturationValues()[2] > 94);
        assertTrue(generator.getLastSaturationValues()[2] < 101);
        generator.generate(1, new ConsoleOutputStrategy());
        assertDoesNotThrow(() -> generator.generate(1, new ConsoleOutputStrategy()));
    }

    @Test
    void testBloodPreasureGenerator(){
        BloodPressureDataGenerator generator = new BloodPressureDataGenerator(2);
        assertTrue(generator.getLastSystolicValues().length == 3);
        assertTrue(generator.getLastDiastolicValues().length == 3);
        assertTrue(generator.getLastSystolicValues()[1] > 109);
        assertTrue(generator.getLastSystolicValues()[1] < 131);
        assertTrue(generator.getLastDiastolicValues()[1] > 69);
        assertTrue(generator.getLastDiastolicValues()[1] < 86);
        assertTrue(generator.getLastSystolicValues()[2] > 109);
        assertTrue(generator.getLastSystolicValues()[2] < 131);
        assertTrue(generator.getLastDiastolicValues()[2] > 69);
        assertTrue(generator.getLastDiastolicValues()[2] < 86);
        assertDoesNotThrow(() -> generator.generate(1, new ConsoleOutputStrategy()));
    }

    @Test
    void testBloodLevelsGenerator(){
        BloodLevelsDataGenerator generator = new BloodLevelsDataGenerator(2);
        assertTrue(generator.getBaselineCholesterol().length == 3);
        assertTrue(generator.getBaselineWhiteCells().length == 3);
        assertTrue(generator.getBaselineRedCells().length == 3);
        assertTrue(generator.getBaselineCholesterol()[1] > 149);
        assertTrue(generator.getBaselineCholesterol()[1] < 201);
        assertTrue(generator.getBaselineWhiteCells()[1] > 3.9);
        assertTrue(generator.getBaselineWhiteCells()[1] < 10.1);
        assertTrue(generator.getBaselineRedCells()[1] > 4.4);
        assertTrue(generator.getBaselineRedCells()[1] < 6.1);
        assertTrue(generator.getBaselineCholesterol()[2] > 149);
        assertTrue(generator.getBaselineCholesterol()[2] < 201);
        assertTrue(generator.getBaselineWhiteCells()[2] > 3.9);
        assertTrue(generator.getBaselineWhiteCells()[2] < 10.1);
        assertTrue(generator.getBaselineRedCells()[2] > 4.4);
        assertTrue(generator.getBaselineRedCells()[2] < 6.1);
        assertDoesNotThrow(() -> generator.generate(1, new ConsoleOutputStrategy()));
    }
}
