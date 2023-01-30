package me.lunev.coursework3.services;

import java.io.File;

public interface FilesService {
    boolean saveSocksToFile(String json);

    String readSocksFromFile();

    boolean cleanSocksFile();

    File getSocksFile();

    boolean saveSocksOperationsToFile(String json);

    String readSocksOperationsFromFile();

    boolean cleanSocksOperationsFile();

    File getSocksOperationsFile();
}
