-- ============================================================================
-- V1 — Baseline do schema imobly
--
-- Consolida os scripts anteriores de scripts/initdb (002 a 004).
-- A criação do database em si continua fora do Flyway (ver 001_criar_database.sql).
--
-- Diferença em relação ao script original: as colunas de status passam de
-- CHAR(1) para VARCHAR(1). CHAR faz padding com espaço, o que atrapalha a
-- comparação e a validação de schema do Hibernate.
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Sequences
-- ----------------------------------------------------------------------------
CREATE SEQUENCE IF NOT EXISTS imobly.seq_endereco_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_pessoa_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_imovel_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_contrato_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_vistoria_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_aluguel_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS imobly.seq_pagamento_id START WITH 1 INCREMENT BY 1;

-- ----------------------------------------------------------------------------
-- Cadastro
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS imobly.endereco
(
    id               BIGINT       NOT NULL DEFAULT nextval('imobly.seq_endereco_id'::regclass),
    cep              VARCHAR(9)   NOT NULL,
    logradouro       VARCHAR(255) NOT NULL,
    numero           VARCHAR(20)  NOT NULL,
    complemento      VARCHAR(255) NULL,
    bairro           VARCHAR(150) NOT NULL,
    cidade           VARCHAR(150) NOT NULL,
    uf               VARCHAR(2)   NOT NULL,
    data_criacao     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP    NULL,
    CONSTRAINT endereco_pk PRIMARY KEY (id),
    CONSTRAINT endereco_check_cep CHECK (cep ~ '^[0-9]{8}$'),
    CONSTRAINT endereco_check_uf CHECK (uf IN
                                        ('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG',
                                         'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE',
                                         'TO'))
);

-- Qualificação completa: os dados abaixo são os exigidos num contrato de locação.
CREATE TABLE IF NOT EXISTS imobly.pessoa
(
    id               BIGINT       NOT NULL DEFAULT nextval('imobly.seq_pessoa_id'::regclass),
    nome             VARCHAR(150) NOT NULL,
    cpf              VARCHAR(14)  NOT NULL UNIQUE,
    rg               VARCHAR(20)  NULL,
    nacionalidade    VARCHAR(50)  NULL     DEFAULT 'Brasileiro',
    estado_civil     VARCHAR(50)  NULL,
    profissao        VARCHAR(150) NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    telefone         VARCHAR(20)  NULL,
    data_criacao     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP    NULL,
    CONSTRAINT pessoa_pk PRIMARY KEY (id),
    CONSTRAINT pessoa_check_nome CHECK (btrim(nome) <> '')
);

CREATE TABLE IF NOT EXISTS imobly.endereco_pessoa
(
    id_pessoa   BIGINT      NOT NULL,
    id_endereco BIGINT      NOT NULL,
    tipo        VARCHAR(50) NULL DEFAULT 'RESIDENCIAL',
    CONSTRAINT endereco_pessoa_pk PRIMARY KEY (id_pessoa, id_endereco),
    CONSTRAINT endereco_pessoa_fk_pessoa FOREIGN KEY (id_pessoa) REFERENCES imobly.pessoa (id),
    CONSTRAINT endereco_pessoa_fk_endereco FOREIGN KEY (id_endereco) REFERENCES imobly.endereco (id)
);

CREATE TABLE IF NOT EXISTS imobly.imovel
(
    id                     BIGINT         NOT NULL DEFAULT nextval('imobly.seq_imovel_id'::regclass),
    descricao              VARCHAR(255)   NOT NULL,
    matricula_rgi          VARCHAR(255)   NULL,
    inscricao_iptu         VARCHAR(255)   NULL,
    valor_aluguel_sugerido NUMERIC(10, 2) NOT NULL,
    id_endereco            BIGINT         NOT NULL,
    data_criacao           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao       TIMESTAMP      NULL,
    CONSTRAINT imovel_pk PRIMARY KEY (id),
    CONSTRAINT imovel_endereco_fk FOREIGN KEY (id_endereco) REFERENCES imobly.endereco (id)
);

-- ----------------------------------------------------------------------------
-- Locação
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS imobly.contrato
(
    id                   BIGINT         NOT NULL DEFAULT nextval('imobly.seq_contrato_id'::regclass),
    id_imovel            BIGINT         NOT NULL,
    id_inquilino         BIGINT         NOT NULL,
    id_locador           BIGINT         NOT NULL,
    data_inicio_contrato DATE           NOT NULL,
    data_fim_contrato    DATE           NOT NULL,
    valor_aluguel        NUMERIC(10, 2) NOT NULL,
    dia_vencimento       INT            NOT NULL DEFAULT 5,
    multa_atraso_perc    NUMERIC(5, 2)  NOT NULL DEFAULT 10.00,
    juros_mes_perc       NUMERIC(5, 2)  NOT NULL DEFAULT 1.00,
    tipo_garantia        VARCHAR(50)    NOT NULL,
    status               VARCHAR(1)     NOT NULL DEFAULT 'A',
    data_criacao         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao     TIMESTAMP      NULL,
    CONSTRAINT contrato_pk PRIMARY KEY (id),
    CONSTRAINT contrato_imovel_fk FOREIGN KEY (id_imovel) REFERENCES imobly.imovel (id),
    CONSTRAINT contrato_inquilino_fk FOREIGN KEY (id_inquilino) REFERENCES imobly.pessoa (id),
    CONSTRAINT contrato_locador_fk FOREIGN KEY (id_locador) REFERENCES imobly.pessoa (id),
    CONSTRAINT contrato_check_periodo CHECK (data_fim_contrato > data_inicio_contrato),
    CONSTRAINT contrato_check_valor CHECK (valor_aluguel > 0),
    CONSTRAINT contrato_check_dia_vencimento CHECK (dia_vencimento BETWEEN 1 AND 28),
    CONSTRAINT contrato_check_locador_inquilino CHECK (id_locador <> id_inquilino),
    CONSTRAINT contrato_check_status CHECK (status IN ('A', 'E', 'C')),
    CONSTRAINT contrato_check_garantia CHECK (tipo_garantia IN
                                              ('CAUCAO', 'FIADOR', 'SEGURO_FIANCA', 'TITULO_CAPITALIZACAO',
                                               'SEM_GARANTIA'))
);

CREATE TABLE IF NOT EXISTS imobly.contrato_testemunha
(
    id_contrato BIGINT       NOT NULL,
    nome        VARCHAR(150) NOT NULL,
    cpf         VARCHAR(14)  NOT NULL,
    CONSTRAINT contrato_testemunha_pk PRIMARY KEY (id_contrato, cpf),
    CONSTRAINT contrato_testemunha_fk FOREIGN KEY (id_contrato) REFERENCES imobly.contrato (id)
);

CREATE TABLE IF NOT EXISTS imobly.vistoria
(
    id              BIGINT      NOT NULL DEFAULT nextval('imobly.seq_vistoria_id'::regclass),
    id_contrato     BIGINT      NOT NULL,
    tipo            VARCHAR(20) NOT NULL,
    data_vistoria   DATE        NOT NULL,
    descricao_geral TEXT        NOT NULL,
    data_criacao    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT vistoria_pk PRIMARY KEY (id),
    CONSTRAINT vistoria_contrato_fk FOREIGN KEY (id_contrato) REFERENCES imobly.contrato (id),
    CONSTRAINT vistoria_check_tipo CHECK (tipo IN ('ENTRADA', 'SAIDA'))
);

-- ----------------------------------------------------------------------------
-- Financeiro
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS imobly.aluguel
(
    id               BIGINT         NOT NULL DEFAULT nextval('imobly.seq_aluguel_id'::regclass),
    id_contrato      BIGINT         NOT NULL,
    ano_referencia   INT            NOT NULL,
    mes_referencia   INT            NOT NULL,
    data_vencimento  DATE           NOT NULL,
    valor_previsto   NUMERIC(10, 2) NOT NULL,
    valor_pago       NUMERIC(10, 2) NOT NULL DEFAULT 0,
    status           VARCHAR(1)     NOT NULL DEFAULT 'A',
    data_criacao     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP      NULL,
    CONSTRAINT aluguel_pk PRIMARY KEY (id),
    CONSTRAINT aluguel_contrato_fk FOREIGN KEY (id_contrato) REFERENCES imobly.contrato (id),
    CONSTRAINT aluguel_uk_competencia UNIQUE (id_contrato, ano_referencia, mes_referencia),
    CONSTRAINT aluguel_check_mes CHECK (mes_referencia BETWEEN 1 AND 12),
    CONSTRAINT aluguel_check_status CHECK (status IN ('A', 'R', 'P'))
);

CREATE TABLE IF NOT EXISTS imobly.pagamento
(
    id                BIGINT         NOT NULL DEFAULT nextval('imobly.seq_pagamento_id'::regclass),
    id_aluguel        BIGINT         NOT NULL,
    valor_pagamento   NUMERIC(10, 2) NOT NULL,
    data_pagamento    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metodo_pagamento  VARCHAR(50)    NULL,
    comprovante_bytea BYTEA          NULL,
    descricao         VARCHAR(255)   NOT NULL,
    CONSTRAINT pagamento_pk PRIMARY KEY (id),
    CONSTRAINT pagamento_aluguel_fk FOREIGN KEY (id_aluguel) REFERENCES imobly.aluguel (id),
    CONSTRAINT pagamento_check_valor CHECK (valor_pagamento > 0)
);

CREATE INDEX IF NOT EXISTS aluguel_idx_contrato ON imobly.aluguel (id_contrato);
CREATE INDEX IF NOT EXISTS pagamento_idx_aluguel ON imobly.pagamento (id_aluguel);
CREATE INDEX IF NOT EXISTS contrato_idx_imovel_status ON imobly.contrato (id_imovel, status);

COMMENT ON COLUMN imobly.contrato.status IS 'A - ATIVO | E - ENCERRADO | C - CANCELADO';
COMMENT ON COLUMN imobly.aluguel.status IS 'A - ABERTO | R - PARCIAL | P - PAGO';
