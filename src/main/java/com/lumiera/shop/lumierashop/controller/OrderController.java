package com.lumiera.shop.lumierashop.controller;

import com.lumiera.shop.lumierashop.domain.Order;
import com.lumiera.shop.lumierashop.domain.enums.OrderStatus;
import com.lumiera.shop.lumierashop.dto.request.CreateOrderForm;
import com.lumiera.shop.lumierashop.dto.request.OrderSearchCondition;
import com.lumiera.shop.lumierashop.global.common.pagination.PageHandler;
import com.lumiera.shop.lumierashop.global.common.pagination.PaginationService;
import com.lumiera.shop.lumierashop.global.security.CustomUserDetails;
import com.lumiera.shop.lumierashop.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class OrderController {

    private static final String ORDER_LIST_VIEW = "order/orderList";
    private static final String ORDER_VIEW = "order/order";
    private static final String REDIRECT_ORDER = "redirect:/orders/";
    private static final String REDIRECT_ORDER_LIST = "redirect:/orders";

    private final OrderService orderService;
    private final PaginationService paginationService;

    @GetMapping
    public String getOrderList(
            @ModelAttribute("searchCondition") OrderSearchCondition searchCondition,
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LocalDateTime startDateTime = searchCondition.getStartDateTimeOrNull();
        LocalDateTime endDateTime = searchCondition.getEndDateTimeOrNull();
        OrderStatus status = searchCondition.getStatus();
        String username = userDetails.getUsername();

        int totalCount = orderService.getOrderCount(startDateTime, endDateTime, status, username);

        PageHandler pageHandler = paginationService.of(
                searchCondition.getPageOrDefault(),
                searchCondition.getSizeOrDefault(),
                totalCount
        );

        List<Order> orderList = orderService.getOrderList(
                pageHandler.getOffset(),
                pageHandler.getLimit(),
                startDateTime,
                endDateTime,
                status,
                username
        );

        model.addAttribute("orderList", orderList);
        model.addAttribute("pageHandler", pageHandler);
        model.addAttribute("searchCondition", searchCondition);

        return ORDER_LIST_VIEW;
    }

    @PostMapping("/create")
    public String createOrder(
            @Valid @ModelAttribute("createForm") CreateOrderForm createForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if (bindingResult.hasErrors()) {
            return REDIRECT_ORDER_LIST;
        }

        String username = userDetails.getUsername();
        List<Long> cartItemIds = createForm.getCartItemIds();

        Long orderId = orderService.createOrder(cartItemIds, username);

        return REDIRECT_ORDER + orderId;
    }

    @GetMapping("/{orderId}")
    public String getOrder(
            @PathVariable Long orderId, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        model.addAttribute("order", orderService.getOrder(orderId, userDetails.getUsername()));
        return ORDER_VIEW;
    }

    @PostMapping("/{orderId}/mock-pay")
    public String mockPay(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        orderService.mockPay(orderId, userDetails.getUsername());
        return REDIRECT_ORDER + orderId;
    }
}