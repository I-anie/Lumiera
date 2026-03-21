package com.lumiera.shop.lumierashop.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private Long categoryId;
    private String thumbnailUrl;
    private String name;
    private int price;
    private int stockQuantity;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
