package com.sibis.order.OrderService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderStockMovement {
    private Long orderId;
    private User user;
    private Item item;
    private Integer quantity;
    private LocalDate creationDate;
    private EnumOrderStatus status;
    private Long stockMovementId;
    private LocalDate stockMovementCreationDate;
}