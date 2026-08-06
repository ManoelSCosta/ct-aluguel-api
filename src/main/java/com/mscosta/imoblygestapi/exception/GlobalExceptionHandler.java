package com.mscosta.imoblygestapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ERRO_VALIDACAO = "Requisição inválida";
    private static final String ERRO_INTEGRIDADE = "Operação viola uma restrição de integridade dos dados";

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroDetailsDTO> handleNotFoundException(NotFoundException exception) {
        final var errorDetails = new ErroDetailsDTO(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDetails);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroDetailsDTO> handleBusinessException(BusinessException exception) {
        final var errorDetails = new ErroDetailsDTO(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorDetails);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDetailsDTO> handleValidationException(MethodArgumentNotValidException exception) {
        final Map<String, String> campos = new HashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(erro -> campos.put(erro.getField(), mensagemDe(erro)));

        final var errorDetails = new ErroDetailsDTO(ERRO_VALIDACAO, null, campos);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Traduz violações de constraint do banco (unicidade de CPF/e-mail, checks do
     * contrato) sem devolver ao cliente a mensagem crua do PostgreSQL.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroDetailsDTO> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        log.warn("Violação de integridade ao persistir", exception);
        final var errorDetails = new ErroDetailsDTO(ERRO_INTEGRIDADE, null);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDetails);
    }

    private String mensagemDe(FieldError erro) {
        return erro.getDefaultMessage() != null ? erro.getDefaultMessage() : ERRO_VALIDACAO;
    }
}
