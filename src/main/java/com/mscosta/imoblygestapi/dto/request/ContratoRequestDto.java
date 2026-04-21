package com.mscosta.imoblygestapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContratoRequestDto(
    Long idImovel,
    Long idInquilino,
    Long idLocador,
    LocalDate dataInicioContrato,
    LocalDate dataFimContrato,
    BigDecimal valorAluguel
) {}
