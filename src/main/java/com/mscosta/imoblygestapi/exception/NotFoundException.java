package com.mscosta.imoblygestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = NotFoundException.REASON)
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    static final String REASON = "Não foi encontrado um registro correspondente";

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
