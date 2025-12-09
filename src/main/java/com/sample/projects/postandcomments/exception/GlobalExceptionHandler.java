package com.sample.projects.postandcomments.exception;

import com.sample.projects.postandcomments.dto.CommonResponse;
import com.sample.projects.postandcomments.util.Constants;
import com.sample.projects.postandcomments.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Resource not found: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.NOT_FOUND, ex.getMessage(), List.of(ex.getMessage()), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CommonResponse<Object>> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        log.warn("Validation failed: {} - Errors: {} - Path: {}", ex.getMessage(), ex.getErrors(), request.getRequestURI());
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrors(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> String.format("%s: %s", error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        
        log.warn("Method argument validation failed: {} - Path: {}", errors, request.getRequestURI());
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.BAD_REQUEST, Constants.VALIDATION_FAILED, errors, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonResponse<Object>> handleConstraintViolationException(
            ConstraintViolationException ex, HttpServletRequest request) {
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        
        log.warn("Constraint validation failed: {} - Path: {}", errors, request.getRequestURI());
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.BAD_REQUEST, Constants.VALIDATION_FAILED, errors, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.warn("Illegal argument: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.BAD_REQUEST, ex.getMessage(), List.of(ex.getMessage()), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred: {} - Path: {}", ex.getMessage(), request.getRequestURI(), ex);
        CommonResponse<Object> response = ResponseUtil.buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", 
                List.of(ex.getMessage() != null ? ex.getMessage() : "Internal server error"), request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

