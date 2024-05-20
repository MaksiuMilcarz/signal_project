package dataManagement;

import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cardioGenerator.outputs.WebSocketOutputStrategy;
import com.dataManagement.DataStorage;
import com.dataManagement.WebSocketDataReader;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorHandingTest {
    private WebSocketOutputStrategy webSocketOutputStrategy;
    private WebSocketServer server;
    private WebSocketDataReader reader;
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() throws Exception {
        int port = 8050;
        webSocketOutputStrategy = new WebSocketOutputStrategy(port);
        dataStorage = new DataStorage();
        server = webSocketOutputStrategy.getServer();
        Thread.sleep(1000); // Wait for the server to start
        reader = new WebSocketDataReader(new URI("ws://localhost:" + port), dataStorage);
        reader.connectBlocking();
    }

    @AfterEach
    void tearDown() throws Exception {
        if (reader != null) {
            reader.closeBlocking();  // Wait for connection to close
        }
        if (server != null) {
            server.stop();
        }
    }

    @Test
    void testDataTransmissionErrorHandling() throws Exception {
        String invalidMessage = "invalid_message";
        webSocketOutputStrategy.output(1, 1, "ECG", invalidMessage);
        assertEquals(0, dataStorage.getAllPatients().size());

        String validMessage = "117";
        webSocketOutputStrategy.output(1, 1, "ECG", validMessage);
        Thread.sleep(500);
        assertEquals(1, dataStorage.getAllPatients().size());
        assertEquals(117, dataStorage.getRecords(1, 0, 10).get(0).getMeasurementValue());
        reader.stopReadingData();
        tearDown();
    }
}
