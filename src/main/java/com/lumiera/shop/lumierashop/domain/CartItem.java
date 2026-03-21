package com.lumiera.shop.lumierashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    private Long id;
    private String username;
    private Long productId;
    private int quantity;

    public CartItem(Long productId) {
        this.productId = productId;
    }

    public CartItem(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
