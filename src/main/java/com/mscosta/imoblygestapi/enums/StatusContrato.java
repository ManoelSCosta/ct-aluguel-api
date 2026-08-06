package com.mscosta.imoblygestapi.enums;

import java.util.Arrays;

public enum StatusContrato implements EnumCodificado {

    ATIVO("A", "Ativo"),
    ENCERRADO("E", "Encerrado"),
    CANCELADO("C", "Cancelado");

    private final String codigo;
    private final String descricao;

    StatusContrato(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    public static StatusContrato fromCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(status -> status.codigo.equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de status de contrato inválido: " + codigo));
    }
}
