package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Client;

import java.util.List;

public interface ClientService {
    Client create(Client client);
    Client readById(long id);
    Client update(Client client, long id);
    void delete(long id);
    List<Client> getAll();
    List<Client> getClientsByAccountId(long accountId);
}
