package com.cardio_generator.outputs;

/**
 * Output strategy interface
 */
public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
