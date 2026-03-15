package com.lumiera.shop.lumierashop.mapper;

import com.lumiera.shop.lumierashop.dto.response.ProductListResponse;
import com.lumiera.shop.lumierashop.dto.response.ProductResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    List<ProductListResponse> findAllAndDeletedAtIsNull(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId
    );

    ProductResponse findByIdAndDeletedAtIsNull(@Param("productId") Long productId);

    int countAndDeletedAtIsNull(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId
    );

    int findStockQuantity(@Param("productId") Long productId);

    int decreaseStockQuantity(@Param("productId") Long productId, @Param("quantity") int quantity);
}
