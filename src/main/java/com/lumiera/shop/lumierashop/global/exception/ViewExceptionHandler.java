package com.lumiera.shop.lumierashop.global.exception;

import com.lumiera.shop.lumierashop.global.exception.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ViewExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException e, HttpServletResponse response, Model model) {
        int status = e.getStatus().value();
        response.setStatus(status);

        model.addAttribute("status", status);
        model.addAttribute("code", e.getErrorCode().name());
        model.addAttribute("message", e.getMessage());

        return "error/error";
    }
}
