package com.lumiera.shop.lumierashop.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemForm {

    @NotNull(message = "상품 정보가 필요합니다.")
    private Long productId;

    @NotNull(message = "수량을 입력해주세요.")
    @Positive(message = "수량은 최소 1개 이상이어야 합니다.")
    private int quantity;
}
