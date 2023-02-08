package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Account;
import com.blankerdog.carService.model.Client;
import com.blankerdog.carService.model.Order;

import java.util.List;
import java.util.stream.Collectors;

public class ClientTransformer {

    public static ClientDto convertToDto(Client client){
        return new ClientDto(
                client.getId(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getEmail(),
                client.getAccount().getId(),
                client.getOrders().stream().map(x -> x.getId()).collect(Collectors.toList())
        );
    }

    public static Client convertToEntity(ClientDto clientDto, Account account, List<Order> orders){
        return new Client(
                clientDto.getId(),
                clientDto.getFirstName(),
                clientDto.getLastName(),
                clientDto.getPhoneNumber(),
                clientDto.getEmail(),
                account,
                orders
        );
    }
}
