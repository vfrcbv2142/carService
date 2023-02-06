package com.blankerdog.carService.repository;

import com.blankerdog.carService.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> getPriceByAccountId(long accountId);
}
