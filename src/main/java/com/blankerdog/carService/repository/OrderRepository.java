package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Override
    List<Order> findAllById(Iterable<Long> ordersIds);

    List<Order> findAllByAccountIdOrderByCreationDateDesc(long accountId);
}
