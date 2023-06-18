package me.lunev.coursework3.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.lunev.coursework3.model.Sock;
import me.lunev.coursework3.services.FilesService;
import me.lunev.coursework3.services.SockService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

/**
 * The service class containing the implementation of the interface {@link SockService}
 *
 * @see Sock
 * @see FilesService
 */
@Service
public class SockServiceImpl implements SockService {

    private static Set<Sock> socks = new HashSet<>();

    private final FilesService filesService;

    public SockServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    private void init() {
        try {
            readSocksFromFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Sock arrivalSocks(Sock sock) {
        if (socks.contains(sock)) {
            int oldCount = 0;
            for (Sock sock1 : socks) {
                if (sock1.equals(sock)) {
                    oldCount = sock1.getQuantity();
                }
                sock1.setQuantity(oldCount + sock.getQuantity());
                sock1.setStockAvailability(true);
                saveSocksToFile();
                return sock1;
            }
        }
        socks.add(sock);
        saveSocksToFile();
        return sock;
    }

    @Override
    public Sock deliveryOrMinusSocks(Sock sock) {
        if (socks.contains(sock)) {
            int oldCount;
            for (Sock sock1 : socks) {
                if (sock1.equals(sock)) {
                    oldCount = sock1.getQuantity();
                    if (oldCount - sock.getQuantity() < 0) {
                        throw new IllegalArgumentException("Товара нет на складе в нужном количестве");
                    }
                    sock1.setQuantity(oldCount - sock.getQuantity());
                    if (sock1.getQuantity() == 0) {
                        sock1.setStockAvailability(false);
                    }
                }
                saveSocksToFile();
                return sock1;
            }
        }
        saveSocksToFile();
        return null;
    }

    @Override
    public Integer getSumSocks(String color,
                               float size,
                               Integer cottonMin,
                               Integer cottonMax) {
        if (cottonMin == null && cottonMax == null) {
            throw new IllegalArgumentException("Параметры запроса отсутствуют");
        }
        if (cottonMin == null) {
            cottonMin = 0;
        }
        if (cottonMax == null) {
            cottonMax = 100;
        }
        if (cottonMin < 0 || cottonMax > 100) {
            throw new IllegalArgumentException("Параметры запроса имеют некорректный формат");
        }
        Integer sumSocks = 0;
        for (Sock sock : socks) {
            if (sock.getColor().equals(color) && sock.getSize() == size && sock.getCottonPart() >= cottonMin && sock.getCottonPart() <= cottonMax) {
                sumSocks += sock.getQuantity();
            }
        }
        return sumSocks;
    }

    @Override
    public void readSocksFromFile() {
        try {
            String json = filesService.readSocksFromFile();
            socks = new ObjectMapper().readValue(json, new TypeReference<Set<Sock>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveSocksToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socks);
            filesService.saveSocksToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
