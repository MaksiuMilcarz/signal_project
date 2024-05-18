package com.dataManagement;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MyWebSocketClient extends WebSocketClient implements ClientInterface, DataReader {
    private DataStorage dataStorage;    

    public MyWebSocketClient(URI serverUri, DataStorage dataStorage) {
        super(serverUri);
        this.dataStorage = dataStorage;
    }

    @Override
    public void readData(DataStorage dataStorage) {
        
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed");
    }

    @Override
    public void onMessage(String message) {
        // Custom logic for when a message is received
        System.out.println("Custom onMessage logic: " + message);
    }

    @Override
    public void onError(Exception ex) {
        // Custom logic for when an error occurs
        System.err.println("Custom onError logic: " + ex.getMessage());
    }

    @Override
    public void connect(String serverUri) throws IOException {
        try {
            URI uri = new URI(serverUri);
            MyWebSocketClient client = new MyWebSocketClient(uri, this.dataStorage);
            client.connectBlocking(); 
        } catch (URISyntaxException | InterruptedException e) {
            throw new IOException("Failed to connect to server", e);
        }
    }

    @Override
    public void disconnect() throws IOException {
        try {
            this.closeBlocking();
        } catch (InterruptedException e) {
            throw new IOException("Failed to disconnect from server", e);
        }
    }
}