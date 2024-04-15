package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 *interface for generating patient data
 */
public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
