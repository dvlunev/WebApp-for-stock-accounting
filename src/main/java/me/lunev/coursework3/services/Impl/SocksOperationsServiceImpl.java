package me.lunev.coursework3.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import me.lunev.coursework3.model.Sock;
import me.lunev.coursework3.model.SockOperation;
import me.lunev.coursework3.services.FilesService;
import me.lunev.coursework3.services.SocksOperationsService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@Service
public class SocksOperationsServiceImpl implements SocksOperationsService {

    protected static ArrayList<SockOperation> socksOperations = new ArrayList<>();

    private final FilesService filesService;

    public SocksOperationsServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        try {
            readSocksOperationsFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addSockOperationArrival(Sock sock) {
        Sock sockInOperation = new Sock(
                sock.getColor(),
                sock.getSize(),
                sock.getCottonPart(),
                sock.getQuantity(),
                true);
        SockOperation sockOperation = new SockOperation("Приемка", sockInOperation);
        socksOperations.add(sockOperation);
        saveSocksOperationsToFile();
    }

    @Override
    public void addSockOperationDelivery(Sock sock) {
        SockOperation sockOperation = new SockOperation("Выдача", sock);
        socksOperations.add(sockOperation);
        saveSocksOperationsToFile();
    }

    @Override
    public void addSockOperationMinus(Sock sock) {
        SockOperation sockOperation = new SockOperation("Списание", sock);
        socksOperations.add(sockOperation);
        saveSocksOperationsToFile();
    }

    public void readSocksOperationsFromFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = filesService.readSocksOperationsFromFile();
            socksOperations = objectMapper.readValue(json, new TypeReference<ArrayList<SockOperation>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveSocksOperationsToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            String json = objectMapper.writeValueAsString(socksOperations);
            filesService.saveSocksOperationsToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
