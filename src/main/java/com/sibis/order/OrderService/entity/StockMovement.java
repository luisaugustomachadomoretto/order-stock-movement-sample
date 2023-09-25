package com.sibis.order.OrderService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "TB_STOCK_MOVEMENT")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "stock_movement_id")
    private Long id;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @ManyToOne
    private Item item;

    @Column(name = "quantity")
    private Integer quantity;

    public void increaseQuantity(Integer newQuantity){
        this.quantity+= newQuantity;
    }

    public void decreaseQuantity(Integer newQuantity){
        this.quantity-=newQuantity;
    }
}
