package com.mscosta.imoblygestapi.dto.response;

public record PessoaResponseDto(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone
) {
}
