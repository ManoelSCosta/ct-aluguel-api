package com.mscosta.imoblygestapi.enums;

public enum StatusAluguel {
    A("Aberto"),
    R("Parcial"),
    P("Pago");

    private final String descricao;

    StatusAluguel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    public static StatusAluguel fromDescricao(String descricao) {
        for (StatusAluguel status : StatusAluguel.values()) {
            if (status.getDescricao().equalsIgnoreCase(descricao)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Descrição de status inválida: " + descricao);
    }
}
