package com.dataManagement;

import java.net.URI;
import java.util.Arrays;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketDataReader extends WebSocketClient implements ContinousDataReader {
    private DataStorage dataStorage;

    public WebSocketDataReader(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void startReadingData() {
        this.connect();
    }

    @Override
    public void stopReadingData() {
        this.close();
    }

    private void processData(String message) {
        try {
            String[] parts = message.split(",");
            // Add logging to inspect message parts
            System.out.println("Message Parts: " + Arrays.toString(parts));
            
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String label = parts[2];
            double data = Double.parseDouble(parts[3]);
            // Add logging to inspect parsed data
            System.out.println("Parsed Data - Patient ID: " + patientId + ", Timestamp: " + timestamp + ", Label: " + label + ", Measurement Value: " + data);
    
            this.dataStorage.addPatientData(patientId, data, label, timestamp);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing message: " + e.getMessage());
            onError(e);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to a WebSocket server");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection to WebSocket server closed");
    }

    @Override
    public void onMessage(String message) {
        processData(message);
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("Error occurred in WebSocket connection: " + ex.getMessage());
    }
}
