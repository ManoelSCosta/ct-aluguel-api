package com.mscosta.imoblygestapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoRequestDto(
    String descricao,
    BigDecimal valorPagamento,
    LocalDateTime dataPagamento,
    Long idAluguel
) {}
