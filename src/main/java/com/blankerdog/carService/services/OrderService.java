package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Order;

import java.util.List;

public interface OrderService {
    Order create(Order order);
    Order readById(long id);
    Order update(Order order, long id);
    void delete(long id);
    List<Order> getAll();
    List<Order> findAllByIds(List<Long> ordersIds);

    List<Order> findAllByAccountId(long accountId);
}
