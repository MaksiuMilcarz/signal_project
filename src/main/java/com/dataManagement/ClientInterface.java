package com.dataManagement;

import java.io.IOException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public interface ClientInterface {
    void onOpen(ServerHandshake handshakedata);
    void onClose(int code, String reason, boolean remote);
    void onMessage(String message);
    void onError(Exception ex);
    void connect(String serverUri) throws IOException;
    void disconnect() throws IOException;
}
