package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTransformer {

    public static OrderDto convertToDto(Order order){
        return new OrderDto(
                order.getId(),
                order.getName(),
                order.getAccount().getId(),
                order.getClient().getId(),
                order.getCreationDate(),
                order.getEmployees().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList()),
                order.getItems().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList()),
                order.getNotes().stream()
                        .map(x -> x.getId())
                        .collect(Collectors.toList())
        );
    }

    public static Order convertToEntity(OrderDto orderDto, Account account, Client client, List<Employee> employees,
                                        List<Item> items, List<Note> notes){
        return new Order(orderDto.getId(),
                orderDto.getName(),
                account,
                client,
                orderDto.getCreationDate(),
                employees,
                items,
                notes
        );
    }
}
