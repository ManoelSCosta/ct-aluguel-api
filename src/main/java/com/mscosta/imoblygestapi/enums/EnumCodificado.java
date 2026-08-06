package com.mscosta.imoblygestapi.enums;

/**
 * Enum cujo valor persistido no banco é um código curto e estável, independente
 * do nome da constante em Java. Permite renomear a constante sem migração de dados.
 */
public interface EnumCodificado {

    String getCodigo();

    String getDescricao();
}
