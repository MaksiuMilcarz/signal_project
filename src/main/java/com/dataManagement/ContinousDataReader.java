package com.dataManagement;

public interface ContinousDataReader {
    void startReadingData(DataStorage dataStorage);
    void stopReadingData();
}