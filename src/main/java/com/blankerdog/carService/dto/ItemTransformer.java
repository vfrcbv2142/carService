package com.blankerdog.carService.dto;

import com.blankerdog.carService.model.Item;
import com.blankerdog.carService.model.Order;
import com.blankerdog.carService.model.WorkType;

import java.util.stream.Collectors;

public class ItemTransformer {

    public static ItemDto convertToDto(Item item){
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getOrder().getId(),
                item.getItemPrice().entrySet().stream()
                        .collect(Collectors.toMap(x -> x.getKey().getName(), y -> y.getValue()))
        );
    }

    public static Item convertToEntity(ItemDto itemDto, Order order){
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                order,
                itemDto.getItemPrice().entrySet().stream()
                        .collect(Collectors.toMap(x -> WorkType.valueOf((x.getKey()).toUpperCase()), y -> y.getValue()))
        );
    }
}
