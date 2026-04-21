package com.mscosta.imoblygestapi.dto.request;

public record ImovelRequestDto(
    String descricao,
    EnderecoRequestDto endereco,
    int page,
    int size
) {}
