package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.Order;
import com.lumiera.shop.lumierashop.domain.enums.OrderStatus;
import com.lumiera.shop.lumierashop.dto.response.OrderResponse;
import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.AdminOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.lumiera.shop.lumierashop.global.exception.code.ErrorCode.ORDER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminOrderService {

    private final AdminOrderMapper orderMapper;

    public List<Order> getOrderList(
            int offset, int limit, LocalDateTime startDateTime, LocalDateTime endDateTime,
            OrderStatus status, String username, boolean deleted
    ) {
        return orderMapper.findAll(offset, limit, startDateTime, endDateTime, status, username, deleted);
    }

    public OrderResponse getOrder(Long orderId) {
        OrderResponse order = orderMapper.findById(orderId);

        if (order == null) {
            throw new CustomException(ORDER_NOT_FOUND);
        }

        return order;
    }

    public int getOrderCount(
            LocalDateTime startDateTime, LocalDateTime endDateTime,
            OrderStatus status, String username, boolean deleted
    ) {
        return orderMapper.count(startDateTime, endDateTime, status, username, deleted);
    }

    @Transactional
    public int changeToPreparing(List<Long> orderIds) {
        return orderMapper.updateStatus(orderIds);
    }
}