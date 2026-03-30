package com.mscosta.imoblygestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroDetailsDTO> handleNotFoundException(NotFoundException exception) {
        var errorDetails = new ErroDetailsDTO(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }
}
