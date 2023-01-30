package me.lunev.coursework3.services.Impl;

import me.lunev.coursework3.services.FilesService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class FilesServiceImpl implements FilesService {

    @Value("${path.to.socks.file}")
    private String socksFilePath;
    @Value("${name.of.socks.file}")
    private String socksFileName;

    @Value("${path.to.socksOperations.file}")
    private String socksOperationsFilePath;
    @Value("${name.of.socksOperations.file}")
    private String socksOperationsFileName;

    @Override
    public boolean saveSocksToFile(String json) {
        try {
            cleanSocksFile();
            Files.writeString(Path.of(socksFilePath, socksFileName),json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readSocksFromFile() {
        try {
            Path path = Path.of(socksFilePath, socksFileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.writeString(path,"[]");
            }
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cleanSocksFile() {
        try {
            Path path = Path.of(socksFilePath, socksFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getSocksFile() {
        return new File(socksFilePath + "/" + socksFileName);
    }

    @Override
    public boolean saveSocksOperationsToFile(String json) {
        try {
            cleanSocksOperationsFile();
            Files.writeString(Path.of(socksOperationsFilePath, socksOperationsFileName),json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String readSocksOperationsFromFile() {
        try {
            Path path = Path.of(socksOperationsFilePath, socksOperationsFileName);
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.writeString(path,"[]");
            }
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean cleanSocksOperationsFile() {
        try {
            Path path = Path.of(socksOperationsFilePath, socksOperationsFileName);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public File getSocksOperationsFile() {
        return new File(socksOperationsFilePath + "/" + socksOperationsFileName);
    }
}
