package com.lumiera.shop.lumierashop.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    PRODUCT_BAD_REQUEST("PRODUCT_400", HttpStatus.BAD_REQUEST, "잘못된 상품 정보가 포함되어 있습니다."),
    PRODUCT_NOT_FOUND("PRODUCT_404", HttpStatus.NOT_FOUND, "일치하는 상품 정보를 찾을 수 없습니다."),
    INSUFFICIENT_STOCK("PRODUCT_409", HttpStatus.CONFLICT, "상품의 재고가 부족합니다."),

    FILE_UPLOAD_FAILED("FILE_UPLOAD_500", HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    FILE_NOT_FOUND("FILE_404", HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

    CART_QUANTITY_EXCEEDS_STOCK("CART_400", HttpStatus.BAD_REQUEST, "상품 수량이 재고 수량을 초과했습니다."),
    CART_ITEM_NOT_FOUND("CART_404", HttpStatus.NOT_FOUND, "장바구니에 담긴 상품이 존재하지 않습니다."),

    ORDER_NOT_FOUND("ORDER_404", HttpStatus.NOT_FOUND, "일치하는 주문 정보를 찾을 수 없습니다."),
    INVALID_ORDER_STATUS("ORDER_409", HttpStatus.CONFLICT, "이미 결제된 상품입니다."),
    ORDER_ITEM_NOT_FOUND("ORDER_404", HttpStatus.BAD_REQUEST, "주문 상품이 존재하지 않습니다."),
    ;

    private String code;
    private HttpStatus status;
    private String message;
}
