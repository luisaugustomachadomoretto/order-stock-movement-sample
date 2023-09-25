package com.sibis.order.OrderService.service;

import com.sibis.order.OrderService.entity.StockMovement;
import com.sibis.order.OrderService.repository.StockMovementRepository;
import com.sibis.order.OrderService.entity.OrderStockMovement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    public List<StockMovement> findAllUMovements(){
        return this.stockMovementRepository.findAll();
    }

    @Transactional
    public StockMovement createOrUpdate(StockMovement s) {
        return this.stockMovementRepository.save(s);
    }
    public List<OrderStockMovement> findAllOrdersWithStockMovements() {
        return this.stockMovementRepository.findOrderStockMovements();
    }

    @Transactional
    public void delete(Long movementID){
        this.stockMovementRepository.deleteById(movementID);
    }
}
