package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Item;
import com.blankerdog.carService.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOrderId(long orderId);

}
