package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Order;
import com.blankerdog.carService.repository.OrderRepository;
import com.blankerdog.carService.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl  implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order create(Order order) {
        if (order != null){
            return orderRepository.save(order);
        }
        throw new NullPointerException("Order cannot be 'null'");
    }

    @Override
    public Order readById(long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + id + " not found"));
    }

    @Override
    public Order update(Order order, long id) {
        Order existingOrder = readById(id);
        existingOrder.setName(order.getName());
        existingOrder.setClient(order.getClient());
        existingOrder.setAccount(order.getAccount());
        existingOrder.setItems(order.getItems());
        existingOrder.setEmployees(order.getEmployees());
        existingOrder.setItems(order.getItems());
        existingOrder.setNotes(order.getNotes());
        return orderRepository.save(existingOrder);
    }

    @Override
    public void delete(long id) {
        orderRepository.delete(readById(id));
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> findAllByIds(List<Long> ordersIds){
        return orderRepository.findAllById(ordersIds);
    }

    @Override
    public List<Order> findAllByAccountId(long accountId){
        return orderRepository.findAllByAccountIdOrderByCreationDateDesc(accountId);
    }
}
