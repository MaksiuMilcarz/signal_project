package com;

import java.io.IOException;

import com.cardioGenerator.HealthDataSimulator;
import com.dataManagement.DataStorage;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            DataStorage.main(new String[]{});
        } else {
            HealthDataSimulator.main(new String[]{});
        }
    }
}
