package me.lunev.coursework3.services;

import java.io.File;

/**
 * Service class interface FilesServiceImpl containing a set of CRUD operations on a file
 */
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
