package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.OrderItem;
import com.lumiera.shop.lumierashop.dto.response.CartResponse;
import com.lumiera.shop.lumierashop.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;

    public void createOrderItem(Long orderId, List<CartResponse> cartItems) {
        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> new OrderItem(
                        orderId,
                        cartItem.getPrice(),
                        cartItem.getProductId(),
                        cartItem.getQuantity()
                ))
                .toList();

        orderItemMapper.save(orderItems);
    }
}
