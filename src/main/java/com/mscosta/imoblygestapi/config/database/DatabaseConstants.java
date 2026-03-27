package com.mscosta.imoblygestapi.config.database;

public class DatabaseConstants {
    private DatabaseConstants() {}

    public static final String SCHEMA_NAME = "imobly";

    public static final class Tables{
        public static final String IMOVEL = "imovel";
        public static final String PESSOA = "pessoa";
        public static final String CONTRATO = "contrato";
        public static final String ALUGUEL = "aluguel";
        public static final String PAGAMENTO = "pagamento";
        public static final String ENDERECO = "endereco";
        public static final String ENDERECO_PESSOA = "endereco_pessoa";
        public static final String ENDERECO_IMOVEL = "endereco_imovel";
    }

    public static final class Sequences {
        public static final String SEQ_IMOVEL_ID = "seq_imovel_id";
        public static final String SEQ_PESSOA_ID = "seq_pessoa_id";
        public static final String SEQ_CONTRATO_ID = "seq_contrato_id";
        public static final String SEQ_ALUGUEL_ID = "seq_aluguel_id";
        public static final String SEQ_PAGAMENTO_ID = "seq_pagamento_id";
        public static final String SEQ_ENDERECO_ID = "seq_endereco_id";
        public static final String SEQ_ENDERECO_PESSOA_ID = "seq_endereco_pessoa_id";
        public static final String SEQ_ENDERECO_IMOVEL_ID = "seq_endereco_imovel_id";
    }
}
