package com.cardio_generator.outputs;

/**
 * Output strategy interface
 * defines the output method
 * Implemented by TcpOutputStrategy, FileOutputStrategy, ConsoleOutputStrategy, WebSocketOutputStrategy
 * @see TcpOutputStrategy
 * @see FileOutputStrategy
 */
public interface OutputStrategy {
    void output(int patientId, long timestamp, String label, String data);
}
