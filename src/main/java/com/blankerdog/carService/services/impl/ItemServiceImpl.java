package com.blankerdog.carService.services.impl;

import com.blankerdog.carService.model.Item;
import com.blankerdog.carService.repository.ItemRepository;
import com.blankerdog.carService.services.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item create(Item item) {
        if (item != null){
            return itemRepository.save(item);
        }
        throw new NullPointerException("Item cannot be 'null'");
    }

    @Override
    public Item readById(long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Item with id " + id + " not found"));
    }

    @Override
    public Item update(Item item, long id) {
        Item existingItem = readById(id);
        existingItem.setName(item.getName());
        existingItem.setItemPrice(item.getItemPrice());
        return itemRepository.save(existingItem);
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(readById(id));
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findAllByOrderId(long orderId){
        return itemRepository.findAllByOrderId(orderId);
    }
}
