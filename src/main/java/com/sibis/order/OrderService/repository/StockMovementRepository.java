package com.sibis.order.OrderService.repository;

import com.sibis.order.OrderService.entity.Item;
import com.sibis.order.OrderService.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    Optional<StockMovement> findStockMovementByItem(Item item);
}
