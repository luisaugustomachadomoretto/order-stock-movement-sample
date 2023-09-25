package com.sibis.order.OrderService.vo;

import lombok.*;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderVO {
    private Long orderID;
    @NonNull
    private Long userID;
    @NonNull
    private Long itemID;
    @NonNull
    private Integer quantity;
}
