package me.lunev.coursework3.services;

import me.lunev.coursework3.model.Sock;

public interface SocksOperationsService {
    void addSockOperationArrival(Sock sock);

    void addSockOperationDelivery(Sock sock);

    void addSockOperationMinus(Sock sock);
}
