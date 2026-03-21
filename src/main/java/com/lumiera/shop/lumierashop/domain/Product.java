package com.lumiera.shop.lumierashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Product {

    private Long id;
    private String thumbnailUrl;
    private Long categoryId;
    private String name;
    private int price;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Product(String thumbnailUrl, Long categoryId, String name, int price, int stockQuantity) {
        this.thumbnailUrl = thumbnailUrl;
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
