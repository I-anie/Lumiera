package com.lumiera.shop.lumierashop.domain;

import com.lumiera.shop.lumierashop.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Order {

    private Long id;
    private String username;
    private int totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Order(int totalPrice, String username) {
        this.totalPrice = totalPrice;
        this.username = username;
    }
}
