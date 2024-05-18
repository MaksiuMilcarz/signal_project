package com.dataManagement;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketDataReader extends WebSocketClient implements ContinousDataReader {
    private DataStorage dataStorage;

    public WebSocketDataReader(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void startReadingData(DataStorage dataStorage) {
        this.connect();
    }

    @Override
    public void stopReadingData() {
        this.close();
    }

    private void processData(String message) {
        try {
            String[] parts = message.split(",");
            
            int patientId = Integer.parseInt(parts[0]);
            long timestamp = Long.parseLong(parts[1]);
            String label = parts[2];
            double data = Double.parseDouble(parts[3]);

            this.dataStorage.addPatientData(patientId,data, label, timestamp);
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

        //As we all n=know very well the only way to solve issues is to turn the power on and off 
        stopReadingData();
        startReadingData(dataStorage);
    }
}
