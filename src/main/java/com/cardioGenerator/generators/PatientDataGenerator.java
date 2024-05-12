package com.cardioGenerator.generators;

import com.cardioGenerator.outputs.OutputStrategy;

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
