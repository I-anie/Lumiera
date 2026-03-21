package com.lumiera.shop.lumierashop.global.exception;

import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import com.lumiera.shop.lumierashop.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.fail(
                        e.getErrorCode().name(),
                        e.getMessage()
                ));
    }
}
