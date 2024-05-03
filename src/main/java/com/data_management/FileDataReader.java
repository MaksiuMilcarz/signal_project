package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.data_management.DataReader;
import com.data_management.DataStorage;

public class FileDataReader implements DataReader{
    public void readData(DataStorage dataStorage) throws IOException {
        // Read data from a file and store it in the data storage
        File directory = new File("/Users/maksiuuuuuuu/Desktop/SoftwareEngineering/project/signal_project/output");
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile() && !file.getName().equals("Alert.txt")) {
                try (FileReader fr = new FileReader(file); BufferedReader reader = new BufferedReader(fr)){
                    Pattern pattern = Pattern.compile("Patient ID: (\\d+), Timestamp: (\\d+), Label: (.*), Data: (\\d+\\.\\d+)");
                    Matcher matcher = pattern.matcher(reader.readLine());
                
                    while (matcher.find()) {
                        int patientId = Integer.parseInt(matcher.group(1));
                        long timestamp = Long.parseLong(matcher.group(2));
                        String recordType = matcher.group(3);
                        double measurementValue;
                        if(file.getName().equals("Saturation.txt")){
                            // Saturation data is in percentage so we need to get rid of the percentage sign
                            measurementValue = Double.parseDouble(matcher.group(4).replace("%", ""));
                        }
                        else{
                            measurementValue = Double.parseDouble(matcher.group(4));
                        }
                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    }
                }
                catch(IOException e){
                    System.out.println("An error occurred while reading the file: " + file.getName());
                    e.printStackTrace();    
                }  
            }

        }
    }  
    public static void main(String[] args) {
        FileDataReader reader = new FileDataReader();
        DataStorage storage = new DataStorage();
        try {
            reader.readData(storage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(storage.getAllPatients().size());
    }
}
