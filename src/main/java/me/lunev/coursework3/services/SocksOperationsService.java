package me.lunev.coursework3.services;

import me.lunev.coursework3.model.Sock;

/**
 * Service class interface SocksOperationsServiceImpl containing a set of CRUD operations on a SocksOperation object
 */
public interface SocksOperationsService {
    void addSockOperationArrival(Sock sock);

    void addSockOperationDelivery(Sock sock);

    void addSockOperationMinus(Sock sock);
}
