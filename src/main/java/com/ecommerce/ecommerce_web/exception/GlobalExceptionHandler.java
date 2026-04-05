package com.ecommerce.ecommerce_web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(
            Exception ex, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Internal Server Error");
        modelAndView.addObject("message", "Ocurrió un error interno del servidor");
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
}

