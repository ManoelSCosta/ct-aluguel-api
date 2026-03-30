package com.mscosta.imoblygestapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = BusinessException.REASON)
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    static final String REASON = "Regra de negócio não atendida";

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
