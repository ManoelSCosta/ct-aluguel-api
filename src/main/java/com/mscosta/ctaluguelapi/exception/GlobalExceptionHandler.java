package com.mscosta.ctaluguelapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroDetailsDTO> handleNotFoundException(NotFoundException e) {
        var defaultError = new ErroDetailsDTO(e.getMessage(), e);
        return new ResponseEntity<>(defaultError, HttpStatus.NOT_FOUND);
    }
}
