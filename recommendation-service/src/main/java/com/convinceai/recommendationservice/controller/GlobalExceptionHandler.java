package com.convinceai.recommendationservice.controller;

import com.convinceai.recommendationservice.dto.ConvinceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ConvinceResponse> handleGenericException(Exception ex) {
        ConvinceResponse response = new ConvinceResponse(
                "error",
                ex.getMessage() != null ? ex.getMessage() : "Unexpected server error",
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
