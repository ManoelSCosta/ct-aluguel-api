package com.mscosta.imoblygestapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ImovelRequestDto(
        @NotBlank @Size(max = 255) String descricao,

        @NotNull @Valid EnderecoRequestDto endereco,

        @NotNull @DecimalMin(value = "0.01", message = "valor sugerido deve ser positivo")
        BigDecimal valorAluguelSugerido,

        @Size(max = 255) String matriculaRgi,
        @Size(max = 255) String inscricaoIptu
) {
}
