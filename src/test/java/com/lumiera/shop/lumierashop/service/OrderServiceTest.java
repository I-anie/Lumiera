package com.lumiera.shop.lumierashop.service;

import com.lumiera.shop.lumierashop.domain.CartItem;
import com.lumiera.shop.lumierashop.domain.Product;
import com.lumiera.shop.lumierashop.domain.User;
import com.lumiera.shop.lumierashop.domain.enums.OrderStatus;
import com.lumiera.shop.lumierashop.dto.response.OrderResponse;
import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import com.lumiera.shop.lumierashop.mapper.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private AdminProductMapper AdminProductMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Test
    @DisplayName("장바구니 상품으로 주문을 생성하면 주문과 주문 상품이 함께 저장한다.")
    void createOrder_success() {
        // given
        String username = "user1";
        authMapper.save(new User(username, "encoded-password"));

        Product product = new Product("/images/test.png", 1L, "상품A", 1000, 10);
        AdminProductMapper.save(product);

        CartItem cartItem = new CartItem(product.getId(), 2);
        cartItem.setUsername(username);
        cartMapper.save(cartItem);

        List<Long> cartItemIds = List.of(cartItem.getId());

        // when
        Long orderId = orderService.createOrder(cartItemIds, username);

        // then
        OrderResponse order = orderMapper.findByIdAndUsername(orderId, username);

        assertThat(order).isNotNull();
        assertThat(order.getUsername()).isEqualTo(username);
        assertThat(order.getTotalPrice()).isEqualTo(2000);
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems().get(0).getProductId()).isEqualTo(product.getId());
        assertThat(order.getOrderItems().get(0).getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("재고가 부족하면 결제에 실패하고 주문 상태와 재고는 변경되지 않는다.")
    void mockPay_fail_whenStockIsInsufficient() {
        // given
        String username = "user1";
        authMapper.save(new User(username, "encoded-password"));

        Product product = new Product("/images/test.png", 1L, "상품A", 1000, 2);
        AdminProductMapper.save(product);

        CartItem cartItem = new CartItem(product.getId(), 3);
        cartItem.setUsername(username);
        cartMapper.save(cartItem);

        Long orderId = orderService.createOrder(List.of(cartItem.getId()), username);

        // when & then
        assertThatThrownBy(() -> orderService.mockPay(orderId, username))
                .isInstanceOf(CustomException.class);

        OrderResponse order = orderMapper.findByIdAndUsername(orderId, username);
        int stockQuantity = productMapper.findStockQuantity(product.getId());

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(stockQuantity).isEqualTo(2);
    }
}