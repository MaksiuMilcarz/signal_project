package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * interface for generating patient data
 * Implemented by BloodSaturationDataGenerator, BloodPressureDataGenerator, BloodLevelsDataGenerator, ECGDataGenerator
 * defines the generate method
 * @see BloodSaturationDataGenerator
 * @see BloodPressureDataGenerator
 * @see BloodLevelsDataGenerator
 * @see ECGDataGenerator
 */
public interface PatientDataGenerator {
    void generate(int patientId, OutputStrategy outputStrategy);
}
