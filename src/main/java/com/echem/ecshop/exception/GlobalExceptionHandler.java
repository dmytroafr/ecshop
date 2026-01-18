package com.echem.ecshop.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model, HttpServletRequest request) {
        log.error("Resource not found - URI: {}, Method: {}, Exception: {}", 
            request.getRequestURI(), request.getMethod(), ex.getMessage());
        model.addAttribute("errorMessage", "Запитуваний ресурс не знайдено");
        model.addAttribute("errorDetails", ex.getMessage());
        model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value());
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        log.error("Bad request - URI: {}, Method: {}, Exception: {}", 
            request.getRequestURI(), request.getMethod(), ex.getMessage());
        model.addAttribute("errorMessage", "Некоректний запит");
        model.addAttribute("errorDetails", ex.getMessage());
        model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model, HttpServletRequest request) {
        log.warn("Access denied - URI: {}, Method: {}, User: {}, Exception: {}", 
            request.getRequestURI(), request.getMethod(), request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous", ex.getMessage());
        model.addAttribute("errorMessage", "Доступ заборонено");
        model.addAttribute("errorDetails", "У вас немає прав для доступу до цього ресурсу");
        model.addAttribute("statusCode", HttpStatus.FORBIDDEN.value());
        return "error";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        log.error("Page not found: {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "Сторінка не знайдена");
        model.addAttribute("errorDetails", "Запитувана сторінка не існує");
        model.addAttribute("statusCode", HttpStatus.NOT_FOUND.value());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model, HttpServletRequest request) {
        log.error("Unexpected error - URI: {}, Method: {}, Exception type: {}", 
            request.getRequestURI(), request.getMethod(), ex.getClass().getSimpleName(), ex);
        model.addAttribute("errorMessage", "Виникла непередбачена помилка");
        model.addAttribute("errorDetails", "Будь ласка, спробуйте пізніше або зв'яжіться з підтримкою");
        model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error";
    }

    // DTO для помилок REST API (якщо знадобиться в майбутньому)
    public static class ErrorResponse {
        private final int status;
        private final String message;
        private final String path;
        private final long timestamp;

        public ErrorResponse(int status, String message, String path) {
            this.status = status;
            this.message = message;
            this.path = path;
            this.timestamp = System.currentTimeMillis();
        }

        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
        public long getTimestamp() { return timestamp; }
    }
}
