package com.mscosta.imoblygestapi.exception;

public class ErroDetailsDTO {

    private final String message;
    private final String exceptionMessage;

    public ErroDetailsDTO(String message, Throwable exception) {
        this.message = message;
        this.exceptionMessage = extractExceptionMessage(exception);
    }

    private static String extractExceptionMessage(Throwable exception) {
        return exception == null ? null : exception.getLocalizedMessage();
    }

    public String getMessage() {
        return message;
    }

    public String getException() {
        return exceptionMessage;
    }
}
