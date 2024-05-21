package com.dataManagement;

/*
 * This interface defines the methods that a class must implement to read data continuously.
 * The interface is implemented by the WebSocketDataReader class.
 * methods enable the client to connect and disconnect at will. 
 */
public interface ContinousDataReader {
    void startReadingData();
    void stopReadingData();
}