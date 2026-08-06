package com.mscosta.imoblygestapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequestDto(
        /* O banco só aceita 8 dígitos sem máscara; validar aqui devolve 400 em vez de 409. */
        @NotBlank @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos, sem máscara")
        String cep,

        @NotBlank @Size(max = 255) String logradouro,
        @NotBlank @Size(max = 20) String numero,
        @NotBlank @Size(max = 150) String bairro,
        @NotBlank @Size(max = 150) String cidade,

        @NotBlank @Pattern(regexp = "[A-Z]{2}", message = "UF deve ter 2 letras maiúsculas")
        String uf,

        @Size(max = 255) String complemento
) {
}
