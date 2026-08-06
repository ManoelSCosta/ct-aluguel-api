package com.mscosta.imoblygestapi.exception;

import java.io.Serial;

/**
 * Exceção de domínio. A tradução para status HTTP é responsabilidade exclusiva
 * do {@link GlobalExceptionHandler}.
 */
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

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
