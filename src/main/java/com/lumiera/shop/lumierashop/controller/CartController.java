package com.lumiera.shop.lumierashop.controller;

import com.lumiera.shop.lumierashop.global.security.CustomUserDetails;
import com.lumiera.shop.lumierashop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart/items")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private static final String CART_VIEW = "cart/cart";

    private final CartService cartService;

    @GetMapping
    public String getCart(
            Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        model.addAttribute("cartItems", cartService.getItems(userDetails.getUsername()));
        return CART_VIEW;
    }
}