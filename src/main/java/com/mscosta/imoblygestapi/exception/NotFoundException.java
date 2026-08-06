package com.mscosta.imoblygestapi.exception;

import java.io.Serial;

/**
 * Exceção de domínio. A tradução para status HTTP é responsabilidade exclusiva
 * do {@link GlobalExceptionHandler}.
 */
public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

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
