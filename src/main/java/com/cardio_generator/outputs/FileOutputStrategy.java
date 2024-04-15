package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Output strategy that writes data to files
 * The data is written to a file in the base directory
 */
//convert class name to UpperCamelCase
public class FileOutputStrategy implements OutputStrategy {  
    //convert baseDirectory var name to camelCase
    private String baseDirectory;  
    //Comvert fileMap var name to camelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>(); 

    /**
     * Constructor
     * @param baseDirectory The base directory where files will be stored
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * tries to write  data to a file in the base directory, if it fails it prints an error message
     * @param patientId The ID of the patient
     * @param timeStamp The timestamp of the data
     * @param label The label of the data
     * @param data The data to be written
     */
    @Override                         //convert timeStamp var name to camelCase
    public void output(int patientId, long timeStamp, String label, String data) { 
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        
        // Set the FilePath variable
        //converted FilePath var name to camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timeStamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}