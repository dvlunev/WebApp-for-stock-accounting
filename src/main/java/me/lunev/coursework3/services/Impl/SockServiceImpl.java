package me.lunev.coursework3.services.Impl;

import me.lunev.coursework3.model.Sock;
import me.lunev.coursework3.services.SockService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SockServiceImpl implements SockService {

    private static Set<Sock> socks = new HashSet<>();

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
                return sock1;
            }
        }
        socks.add(sock);
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
                return sock1;
            }
        }
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
}
