package com.mscosta.imoblygestapi.dto.response;

import java.math.BigDecimal;

public record ImovelResponseDto(
        Long id,
        String descricao,
        String endereco,
        BigDecimal valorAluguelSugerido,
        Boolean disponivel
) {
}
