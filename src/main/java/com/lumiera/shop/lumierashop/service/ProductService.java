package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.dto.response.OrderItemResponse;
import com.lumiera.shop.lumierashop.dto.response.ProductListResponse;
import com.lumiera.shop.lumierashop.dto.response.ProductResponse;
import com.lumiera.shop.lumierashop.global.error.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lumiera.shop.lumierashop.global.error.code.ErrorCode.INSUFFICIENT_STOCK;
import static com.lumiera.shop.lumierashop.global.error.code.ErrorCode.PRODUCT_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductMapper productMapper;

    public List<ProductListResponse> getProductList(
            int offset, int limit, @Nullable String keyword, @Nullable Long categoryId
    ) {
        return productMapper.findAllAndDeletedAtIsNull(offset, limit, keyword, categoryId);
    }

    public ProductResponse getProduct(Long productId) {
        ProductResponse product = productMapper.findByIdAndDeletedAtIsNull(productId);

        if (product == null) {
            throw new CustomException(PRODUCT_NOT_FOUND);
        }

        return product;
    }

    public int getTotalCount(@Nullable String keyword, @Nullable Long categoryId) {
        return productMapper.countAndDeletedAtIsNull(keyword, categoryId);
    }

    public int getStockQuantity(Long productId) {
        return productMapper.findStockQuantity(productId);
    }

    @Transactional
    public void decreaseStockQuantity(List<OrderItemResponse> orderItems) {
        for (OrderItemResponse orderItem : orderItems) {
            int affectedRows = productMapper.decreaseStockQuantity(orderItem.getProductId(), orderItem.getQuantity());

            if (affectedRows == 0) {
                throw new CustomException(INSUFFICIENT_STOCK);
            }
        }
    }
}