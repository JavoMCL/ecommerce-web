package com.ecommerce.ecommerce_web.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("Resource not found: {} - {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ModelAndView handleInvalidInput(InvalidInputException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("Invalid input at {}: {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Bad Request");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView handleUnauthorized(UnauthorizedException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("Unauthorized access attempt to {}: {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.UNAUTHORIZED.value());
        modelAndView.addObject("error", "Unauthorized");
        modelAndView.addObject("message", ex.getMessage());
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
        return modelAndView;
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ModelAndView handleAuthorizationDenied(AuthorizationDeniedException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("Access denied to {}: {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.FORBIDDEN.value());
        modelAndView.addObject("error", "Access Forbidden");
        modelAndView.addObject("message", "You do not have permission to access this resource");
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        return modelAndView;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ModelAndView handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("No resource found for {}: {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.NOT_FOUND.value());
        modelAndView.addObject("error", "Not Found");
        modelAndView.addObject("message", "Resource not found");
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) {
        logger.warn("Validation failed for request {}: {}", request.getRequestURI(), ex.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String validations = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.BAD_REQUEST.value());
        modelAndView.addObject("error", "Bad Request");
        modelAndView.addObject("message", "Validation error: " + validations);
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGlobalException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        logger.error("Unhandled exception for {}", request.getRequestURI(), ex);
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("timestamp", LocalDateTime.now());
        modelAndView.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        modelAndView.addObject("error", "Internal Server Error");
        modelAndView.addObject("message", "An internal server error occurred");
        modelAndView.addObject("path", request.getRequestURI());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }
}