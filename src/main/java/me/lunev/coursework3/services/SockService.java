package me.lunev.coursework3.services;

import me.lunev.coursework3.model.Sock;

/**
 * Service class interface SockServiceImpl containing a set of CRUD operations on a Sock object
 */
public interface SockService {
    Sock arrivalSocks(Sock sock);

    Sock deliveryOrMinusSocks(Sock sock);

    Integer getSumSocks(String color,
                        float size,
                        Integer cottonMin,
                        Integer cottonMax);

    void readSocksFromFile();
}
