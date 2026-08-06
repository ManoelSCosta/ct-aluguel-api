package com.mscosta.imoblygestapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroDetailsDTO {

    private final String message;
    private final String exceptionMessage;
    private final Map<String, String> campos;

    public ErroDetailsDTO(String message, Throwable exception) {
        this(message, exception, null);
    }

    public ErroDetailsDTO(String message, Throwable exception, Map<String, String> campos) {
        this.message = message;
        this.exceptionMessage = extractExceptionMessage(exception);
        this.campos = campos;
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

    /** Preenchido apenas em erros de validação: nome do campo -> motivo da rejeição. */
    public Map<String, String> getCampos() {
        return campos;
    }
}
