package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
