package com.blankerdog.carService.services;

import com.blankerdog.carService.model.Price;

import java.util.List;

public interface PriceService {
    Price create(Price price);
    Price readById(long id);
    Price update(Price price, long id);
    void delete(long id);
    Price getByAccountId(long accountId);
}
