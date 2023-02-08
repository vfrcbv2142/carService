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
                ClientTransformer.convertToDto(order.getClient()),
                order.getCreationDate(),
                order.getEmployees().stream()
                        .map(x -> EmployeeTransformer.convertToDto(x))
                        .collect(Collectors.toList()),
                order.getItems().stream()
                        .map(x -> ItemTransformer.convertToDto(x))
                        .collect(Collectors.toList()),
                order.getNotes()
        );
    }

    public static Order convertToEntity(OrderDto orderDto, Account account, Client client, List<Employee> employees,
                                        List<Item> items){
        return new Order(orderDto.getId(),
                orderDto.getName(),
                account,
                client,
                orderDto.getCreationDate(),
                employees,
                items,
                orderDto.getNotes()
        );
    }
}
