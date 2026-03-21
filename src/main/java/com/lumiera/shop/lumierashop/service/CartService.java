package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.CartItem;
import com.lumiera.shop.lumierashop.dto.request.CartItemForm;
import com.lumiera.shop.lumierashop.dto.response.CartResponse;
import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.CART_ITEM_NOT_FOUND;
import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.CART_QUANTITY_EXCEEDS_STOCK;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartMapper cartMapper;
    private final ProductService productService;

    public List<CartResponse> getItems(String username) {
        return cartMapper.findByUsername(username);
    }

    @Transactional
    public void addItem(CartItemForm cartItemForm, String username) {
        validateQuantityWithinStock(cartItemForm.getProductId(), cartItemForm.getQuantity());

        CartItem cartItem = new CartItem(cartItemForm.getProductId(), cartItemForm.getQuantity());
        cartItem.setUsername(username);

        cartMapper.save(cartItem);
    }

    @Transactional
    public void updateItem(Long itemId, CartItemForm cartItemForm, String username) {
        validateQuantityWithinStock(cartItemForm.getProductId(), cartItemForm.getQuantity());

        int affectedRows = cartMapper.update(itemId, cartItemForm.getQuantity(), username);
        validateAffectedRows(affectedRows);
    }

    @Transactional
    public void deleteItem(Long itemId, String username) {
        int affectedRows = cartMapper.delete(itemId, username);
        validateAffectedRows(affectedRows);
    }

    private void validateQuantityWithinStock(Long productId, int quantity) {
        int stockQuantity = productService.getStockQuantity(productId);

        if (quantity > stockQuantity) {
            throw new CustomException(CART_QUANTITY_EXCEEDS_STOCK);
        }
    }

    private void validateAffectedRows(int affectedRows) {
        if (affectedRows == 0) {
            throw new CustomException(CART_ITEM_NOT_FOUND);
        }
    }
}