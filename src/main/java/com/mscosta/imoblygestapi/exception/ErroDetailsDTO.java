package com.mscosta.imoblygestapi.exception;

import java.time.LocalDateTime;

public class ErroDetailsDTO {
    private final String message;

    private final String exception;

    public ErroDetailsDTO(String message, Exception exception) {
        this.message = message;
        this.exception = exception.getLocalizedMessage();
    }


    public String getMessage() {
        return message;
    }

    public String getException() {
        return exception;
    }
}
