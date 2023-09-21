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
@Table(name = "TB_ORDER")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Item item;

    @Column(name = "quantity",nullable = false)
    private Integer quantity = 0;

    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnumOrderStatus status = EnumOrderStatus.NEW;

}
