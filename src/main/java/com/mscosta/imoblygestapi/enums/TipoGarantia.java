package com.mscosta.imoblygestapi.enums;

/**
 * Garantia locatícia do contrato (Lei 8.245/91, art. 37). Persistido pelo nome
 * da constante — a coluna é VARCHAR(50) e o check do banco usa estes mesmos valores.
 */
public enum TipoGarantia {

    CAUCAO("Caução"),
    FIADOR("Fiador"),
    SEGURO_FIANCA("Seguro-fiança"),
    TITULO_CAPITALIZACAO("Título de capitalização"),
    SEM_GARANTIA("Sem garantia");

    private final String descricao;

    TipoGarantia(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
