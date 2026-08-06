package com.mscosta.imoblygestapi.dto.request;

import com.mscosta.imoblygestapi.enums.TipoGarantia;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Os campos diaVencimento, multaAtrasoPerc e jurosMesPerc são opcionais: quando
 * ausentes, valem os padrões definidos na entidade Contrato.
 */
public record ContratoRequestDto(
        @NotNull Long idImovel,
        @NotNull Long idInquilino,
        @NotNull Long idLocador,
        @NotNull LocalDate dataInicioContrato,
        @NotNull LocalDate dataFimContrato,

        @NotNull @DecimalMin(value = "0.01", message = "valor do aluguel deve ser positivo")
        BigDecimal valorAluguel,

        @NotNull TipoGarantia tipoGarantia,

        @Min(1) @Max(28) Integer diaVencimento,
        @DecimalMin("0.00") BigDecimal multaAtrasoPerc,
        @DecimalMin("0.00") BigDecimal jurosMesPerc
) {
}
