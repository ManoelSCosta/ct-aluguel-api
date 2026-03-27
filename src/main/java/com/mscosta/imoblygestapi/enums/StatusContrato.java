package com.mscosta.imoblygestapi.enums;

public enum StatusContrato {
    A("Ativo"),
    E("Encerrado"),
    C("Cancelado");
    private final String descricao;
    StatusContrato(String descricao) {
        this.descricao = descricao;
    }
    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
