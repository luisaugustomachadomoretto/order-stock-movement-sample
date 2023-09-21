package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.Item;
import com.sibis.order.OrderService.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> findAllItens(){
        return this.itemRepository.findAll();
    }

    @Transactional
    public Item createOrUpdate(Item item) {
        return this.itemRepository.save(item);
    }

    @Transactional
    public void delete(Long orderID){
        this.itemRepository.deleteById(orderID);
    }
}
