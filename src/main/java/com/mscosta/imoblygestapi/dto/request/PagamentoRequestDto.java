package com.mscosta.imoblygestapi.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoRequestDto(
        @NotBlank @Size(max = 255) String descricao,

        @NotNull @DecimalMin(value = "0.01", message = "valor do pagamento deve ser positivo")
        BigDecimal valorPagamento,

        @NotNull @PastOrPresent LocalDateTime dataPagamento,

        @NotNull Long idAluguel
) {
}
