package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);
    Item readById(long id);
    Item update(Item item, long id);
    void delete(long id);
    List<Item> getAll();

    List<Item> findAllByOrderId(long orderId);
}
