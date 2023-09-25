package com.sibis.order.OrderService.repository;

import com.sibis.order.OrderService.entity.Item;
import com.sibis.order.OrderService.entity.StockMovement;
import com.sibis.order.OrderService.entity.OrderStockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    Optional<StockMovement> findStockMovementByItem(Item item);
    @Query("SELECT new com.sibis.order.OrderService.entity.OrderStockMovement(o.id, o.user, o.item, o.quantity, o.creationDate, o.status, sm.id, sm.creationDate) " +
            "FROM Order o " +
            "JOIN StockMovement sm ON o.item.id = sm.item.id")
    List<OrderStockMovement> findOrderStockMovements();
}
