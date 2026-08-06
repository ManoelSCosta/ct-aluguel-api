package com.mscosta.imoblygestapi.dto.request;

import com.mscosta.imoblygestapi.enums.EstadoCivilEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PessoaRequestDto(
        @NotBlank @Size(max = 150) String nome,

        @NotBlank
        @Pattern(regexp = "\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
                message = "CPF deve ter 11 dígitos, com ou sem máscara")
        String cpf,

        @NotBlank @Email @Size(max = 150) String email,

        @Size(max = 20) String telefone,
        @Size(max = 20) String rg,
        @Size(max = 50) String nacionalidade,
        EstadoCivilEnum estadoCivil,
        @Size(max = 150) String profissao
) {
}
