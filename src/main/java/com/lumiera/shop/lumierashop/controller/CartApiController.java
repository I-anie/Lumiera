package com.lumiera.shop.lumierashop.controller;

import com.lumiera.shop.lumierashop.dto.request.CartItemForm;
import com.lumiera.shop.lumierashop.global.response.ApiResponse;
import com.lumiera.shop.lumierashop.global.security.CustomUserDetails;
import com.lumiera.shop.lumierashop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/items")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartApiController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addItem(
            @Valid @RequestBody CartItemForm cartItemForm,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.addItem(cartItemForm, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(
                "장바구니에 상품을 추가했습니다.",
                null
        ));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody CartItemForm cartItemForm,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.updateItem(itemId, cartItemForm, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(
                "장바구니 상품 수량을 변경했습니다.",
                null
        ));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(
            @PathVariable Long itemId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        cartService.deleteItem(itemId, userDetails.getUsername());

        return ResponseEntity.ok(ApiResponse.success(
                "장바구니에서 상품을 제거했습니다.",
                null
        ));
    }
}