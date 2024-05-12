package com.cardioGenerator.generators;

import java.util.Random;

import com.cardioGenerator.outputs.OutputStrategy;

/**
 * Generates alert data for patients
 * alerts are triggered randomly with a given average rate  - lambda - and resolved with a 90% probability
 * Alerts are outputted using the given output strategy
 */
public class AlertGenerator implements PatientDataGenerator {
    //convert randomGenerator var name to UPPEER_CASE, since its a constant
    private static final Random RANDOM_GENERATOR = new Random();
    //convert AlertStates var name to camelCase
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Constructor for the AlertGenerator class
     * @param patientCount The number of patients as specified by the user
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for a patient and outputs it using the given output strategy.
     * Alerts are triggered randomly with a given average rate (lambda) and resolved with a 90% probability.
     * @param patientId 
     * @param outputStrategy
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Convert lambda var name to camelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
