package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Client;
import com.blankerdog.carService.repository.ClientRepository;
import com.blankerdog.carService.repository.ClientRepository;
import com.blankerdog.carService.services.ClientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Client create(Client client) {
        if (client != null){
            return clientRepository.save(client);
        }
        throw new NullPointerException("Client cannot be 'null'");
    }

    @Override
    public Client readById(long id) {
        return clientRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Client with id " + id + " not found"));
    }

    @Override
    public Client update(Client client, long id) {
        Client existingClient = readById(id);
        existingClient.setEmail(client.getEmail());
        existingClient.setFirstName(client.getFirstName());
        existingClient.setLastName(client.getLastName());
        existingClient.setPhoneNumber(client.getPhoneNumber());
        existingClient.setOrders(client.getOrders());
        return clientRepository.save(existingClient);
    }

    @Override
    public void delete(long id) {
        clientRepository.delete(readById(id));
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClientsByAccountId(long accountId){
        return clientRepository.getClientsByAccountId(accountId);

    }
}
