package com.lumiera.shop.lumierashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Long orderId;
    private int price;
    private Long productId;
    private int quantity;

    public OrderItem(Long orderId, int price, Long productId, int quantity) {
        this.orderId = orderId;
        this.price = price;
        this.productId = productId;
        this.quantity = quantity;
    }
}
