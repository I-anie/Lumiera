package com.lumiera.shop.lumierashop.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductForm {

    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;

    @NotBlank(message = "상품명을 입력해주세요.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    @Min(value = 1000, message = "가격은 최소 1000원 부터 입력 가능합니다.")
    private int price;

    @NotNull(message = "재고 수량을 입력해주세요.")
    @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.")
    private int stockQuantity;

    private MultipartFile thumbnail;
    private List<MultipartFile> detailImages;

    public UpdateProductForm(Long categoryId, String name, int price, int stockQuantity) {
        this.categoryId = categoryId;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}
