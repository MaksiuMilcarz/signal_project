package com.dataManagement;

import java.net.URI;
import java.util.Arrays;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/*
 * This class is responsible for reading data from a WebSocket server.
 * It extends the WebSocketClient class from the Java-WebSocket library.
 * The WebSocketDataReader class implements the ContinousDataReader interface.
 * Connects, reads, and processes data from a WebSocket server, then stores the data in a DataStorage object.
 */
public class WebSocketDataReader extends WebSocketClient implements ContinousDataReader {
    private DataStorage dataStorage;
    private URI serverUri;

    public WebSocketDataReader(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.serverUri = serverUri;
        this.dataStorage = dataStorage;
    }

    /*
     * This method connects to the WebSocket server at will
     */
    @Override
    public void startReadingData() {
        WebSocketDataReader client = new WebSocketDataReader(serverUri, dataStorage);
        try {
            Thread.sleep(1000);
            client.connect();
        } catch (Exception e) {
            System.err.println("Error connecting to WebSocket server: " + e.getMessage());
            onError(e);
        }
    }

    /*
     * This method disconnects from the WebSocket server at will
     */
    @Override
    public void stopReadingData() {
        this.close();
    }

    /*
     * This method processes the data received from the WebSocket server.
     * It parses the message and stores the data in the DataStorage object.
     * The message format is: "patientId,timestamp,label,data" (int, long, string, string)
     * The data storage needs: "patientId, data, label, timestamp" (int, double, string, long)
     */
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

    /*
     * This method is called when the WebSocket connection is opened successfully, used for debugging
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to a WebSocket server");
    }

    /*
     * This method is called when the WebSocket connection is closed, used for debugging
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection to WebSocket server closed");
    }

    /*
     * This method is called when a message is received from the WebSocket server automatically
     * sends the data for further processing
     */
    @Override
    public void onMessage(String message) {
        try {
            processData(message);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing message: " + e.getMessage());
        }
    }

    /*
     * This method is called when an error occurs in the WebSocket connection
     * It stops reading data and tries to reconnect after a delay
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("Error occurred in WebSocket connection: " + ex.getMessage());
        ex.printStackTrace();
        if (!this.isClosed()) {
            stopReadingData();
        }
        try {
            stopReadingData();
            Thread.sleep(1500);
            startReadingData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}
