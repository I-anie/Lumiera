package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.Order;
import com.lumiera.shop.lumierashop.domain.enums.OrderStatus;
import com.lumiera.shop.lumierashop.dto.response.CartResponse;
import com.lumiera.shop.lumierashop.dto.response.OrderItemResponse;
import com.lumiera.shop.lumierashop.dto.response.OrderResponse;
import com.lumiera.shop.lumierashop.global.error.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.CartMapper;
import com.lumiera.shop.lumierashop.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.lumiera.shop.lumierashop.domain.enums.OrderStatus.PENDING;
import static com.lumiera.shop.lumierashop.global.error.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemService orderItemService;
    private final CartMapper cartMapper;
    private final ProductService productService;

    public List<Order> getOrderList(
            int offset,
            int limit,
            @Nullable LocalDateTime startDateTime,
            @Nullable LocalDateTime endDateTime,
            @Nullable OrderStatus status,
            String username
    ) {
        return orderMapper.findAllAndDeletedAtIsNull(offset, limit, startDateTime, endDateTime, status, username);
    }

    @Transactional
    public Long createOrder(List<Long> cartItemIds, String username) {
        List<CartResponse> cartItems = cartMapper.findCartItemsByIds(cartItemIds, username);

        if (cartItems.isEmpty() || cartItems.size() != cartItemIds.size()) {
            throw new CustomException(CART_ITEM_NOT_FOUND);
        }

        Order order = new Order(calculateTotalPrice(cartItems), username);
        orderMapper.save(order);

        Long orderId = order.getId();
        orderItemService.createOrderItem(orderId, cartItems);

        return orderId;
    }

    public OrderResponse getOrder(Long orderId, String username) {
        OrderResponse order = orderMapper.findByIdAndUsername(orderId, username);

        if (order == null) {
            throw new CustomException(ORDER_NOT_FOUND);
        }

        return order;
    }

    public int getOrderCount(
            LocalDateTime startDateTime, LocalDateTime endDateTime, OrderStatus status, String username
    ) {
        return orderMapper.countAndDeletedAtIsNull(startDateTime, endDateTime, status, username);
    }

    @Transactional
    public void mockPay(Long orderId, String username) {
        OrderResponse order = getOrder(orderId, username);

        if (!PENDING.equals(order.getStatus())) {
            throw new CustomException(INVALID_ORDER_STATUS);
        }

        List<OrderItemResponse> orderItems = order.getOrderItems();
        if (orderItems.isEmpty()) {
            throw new CustomException(ORDER_ITEM_NOT_FOUND);
        }

        productService.decreaseStockQuantity(orderItems);

        int affectedRows = orderMapper.updateStatus(orderId, username);
        validateAffectedRows(affectedRows);
    }

    private int calculateTotalPrice(List<CartResponse> cartItems) {
        return cartItems.stream()
                .mapToInt(cartItem -> cartItem.getPrice() * cartItem.getQuantity())
                .sum();
    }

    private void validateAffectedRows(int affectedRows) {
        if (affectedRows == 0) {
            throw new CustomException(ORDER_NOT_FOUND);
        }
    }
}