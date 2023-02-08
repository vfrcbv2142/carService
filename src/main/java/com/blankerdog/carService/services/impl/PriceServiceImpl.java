package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Price;
import com.blankerdog.carService.repository.PriceRepository;
import com.blankerdog.carService.services.PriceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Override
    public Price create(Price price) {
        if (price != null){
            return priceRepository.save(price);
        }
        throw new NullPointerException("Price cannot be 'null'");
    }

    @Override
    public Price readById(long id) {
        return priceRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Price with id " + id + " not found"));
    }

    @Override
    public Price update(Price price, long id) {
        Price existingPrice = readById(id);
        existingPrice.setDisassembling(price.getDisassembling());
        existingPrice.setPainting(price.getPainting());
        existingPrice.setPreparingIron(price.getPreparingIron());
        existingPrice.setPreparingAluminum(price.getPreparingAluminum());
        existingPrice.setPreparingPlastic(price.getPreparingPlastic());
        existingPrice.setSoldering(price.getSoldering());
        existingPrice.setStraightening(price.getStraightening());
        return priceRepository.save(existingPrice);
    }

    @Override
    public void delete(long id) {
        priceRepository.delete(readById(id));
    }

    @Override
    public Price getByAccountId(long accountId){
        return priceRepository.getPriceByAccountId(accountId).orElseThrow(
                () -> new EntityNotFoundException("Price not found"));
    }
}
