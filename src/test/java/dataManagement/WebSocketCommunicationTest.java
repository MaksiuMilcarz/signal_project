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

public class WebSocketCommunicationTest {
    private WebSocketOutputStrategy webSocketOutputStrategy;
    private WebSocketServer server;
    private WebSocketDataReader reader;
    private DataStorage dataStorage;

    @BeforeEach
    void setUp() throws Exception {
        int port = 8080;
        webSocketOutputStrategy = new WebSocketOutputStrategy(port);
        dataStorage = DataStorage.getInstance();
        server = webSocketOutputStrategy.getServer();
        Thread.sleep(1000); // Wait for the server to start
        reader = new WebSocketDataReader(new URI("ws://localhost:" + port), dataStorage);
        reader.connectBlocking();
    }

    @AfterEach
    void tearDown() throws Exception{
        if (reader != null) {
            reader.closeBlocking();  // Wait for connection to close
        }
        if (server != null) {
            server.stop();
        }
        DataStorage.removeInstance();
    }

    /*
     * This test checks if the data is stored correctly in the DataStorage object.
     */
    @Test
    void testOutput() throws Exception{
        assertEquals(0, dataStorage.getAllPatients().size());
        webSocketOutputStrategy.output(1, 1, "ECG", "117");
        Thread.sleep(500);
        assertEquals(1, dataStorage.getAllPatients().size());
        assertEquals(117, dataStorage.getRecords(1, 0, 10).get(0).getMeasurementValue());
        reader.stopReadingData();
        tearDown(); 
    }
}
