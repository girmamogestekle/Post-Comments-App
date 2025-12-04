package com.sample.projects.postandcomments.util;

import com.sample.projects.postandcomments.dto.CommonResponse;
import com.sample.projects.postandcomments.dto.response.AiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class ResponseUtil {

    private static final String API_VERSION = "1.0.0";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    private ResponseUtil() {
        // Utility class - prevent instantiation
    }

    public static <T> CommonResponse<T> buildSuccessResponse(
            HttpStatus status, String message, T payload, HttpServletRequest httpRequest) {
        return CommonResponse.<T>builder()
                .status(status.value())
                .message(message)
                .payload(payload)
                .success(true)
                .timestamp(getCurrentTimestamp())
                .path(httpRequest.getRequestURI())
                .traceId(generateTraceId())
                .errors(null)
                .meta(null)
                .apiVersion(API_VERSION)
                .correlationId(generateCorrelationId())
                .build();
    }

    public static <T> CommonResponse<T> buildSuccessResponseWithAiResponse(
            HttpStatus status,
            String message,
            T payload,
            AiResponse aiResponse,
            HttpServletRequest httpRequest) {
        CommonResponse<T> commonResponse = buildSuccessResponse(status, message, payload, httpRequest);
        commonResponse.setAiPayload(aiResponse);
        return commonResponse;
    }

    public static <T> CommonResponse<T> buildErrorResponse(
            HttpStatus status, String message, List<String> errors, HttpServletRequest httpRequest) {
        return CommonResponse.<T>builder()
                .status(status.value())
                .message(message)
                .payload(null)
                .success(false)
                .timestamp(getCurrentTimestamp())
                .path(httpRequest.getRequestURI())
                .traceId(generateTraceId())
                .errors(errors)
                .meta(null)
                .apiVersion(API_VERSION)
                .correlationId(generateCorrelationId())
                .build();
    }

    public static String getCurrentTimestamp() {
        return LocalDateTime.now().format(DATE_TIME_FORMATTER);
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    public static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}

