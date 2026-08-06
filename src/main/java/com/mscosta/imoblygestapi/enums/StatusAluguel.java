package com.mscosta.imoblygestapi.enums;

import java.util.Arrays;

public enum StatusAluguel implements EnumCodificado {

    ABERTO("A", "Aberto"),
    PARCIAL("R", "Parcial"),
    PAGO("P", "Pago");

    private final String codigo;
    private final String descricao;

    StatusAluguel(String codigo, String descricao) {
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

    public static StatusAluguel fromCodigo(String codigo) {
        return Arrays.stream(values())
                .filter(status -> status.codigo.equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código de status de aluguel inválido: " + codigo));
    }
}
