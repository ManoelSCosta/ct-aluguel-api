-- ============================================================================
-- TABELAS
-- ============================================================================

-- Tabela: imovel
CREATE TABLE IF NOT EXISTS imobly.imovel
(
    id           BIGINT NOT NULL DEFAULT nextval('imobly.seq_imovel_id'::regclass),
    descricao    VARCHAR(255) NULL,
    matricula_rgi VARCHAR(255) NULL,
    inscricao_iptu VARCHAR(255) NULL,
    valor_aluguel NUMERIC(10, 2) NOT NULL,
    id_endereco  BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    CONSTRAINT imovel_pk PRIMARY KEY (id),
    CONSTRAINT imovel_check_endereco
        CHECK (btrim(endereco) <> '')
);

-- Tabela: pessoa
CREATE TABLE IF NOT EXISTS imobly.pessoa
(
    id           BIGINT NOT NULL DEFAULT nextval('imobly.seq_pessoa_id'::regclass),
    nome         VARCHAR(150) NOT NULL,
    contato      VARCHAR(150) NOT NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    CONSTRAINT pessoa_pk PRIMARY KEY (id),
    CONSTRAINT pessoa_check_nome
        CHECK (btrim(nome) <> ''),
    CONSTRAINT pessoa_check_contato
        CHECK (btrim(contato) <> '')
);

-- Tabela: contrato
CREATE TABLE IF NOT EXISTS imobly.contrato
(
    id                   BIGINT NOT NULL DEFAULT nextval('imobly.seq_contrato_id'::regclass),
    id_imovel            BIGINT NOT NULL,
    id_inquilino         BIGINT NOT NULL,
    id_locador           BIGINT NOT NULL,
    data_criacao         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao     TIMESTAMP NULL,
    data_inicio_contrato DATE NOT NULL,
    data_fim_contrato    DATE NULL default CURRENT_DATE + INTERVAL '1 year',
    valor_aluguel        NUMERIC(10, 2) NOT NULL,
    status               CHAR(1) NOT NULL,
    CONSTRAINT contrato_pk PRIMARY KEY (id),
    CONSTRAINT contrato_check_valor_aluguel
        CHECK (valor_aluguel > 0),
    CONSTRAINT contrato_check_periodo
        CHECK (data_fim_contrato > data_inicio_contrato),
    CONSTRAINT contrato_check_status
        CHECK (status IN ('A', 'E', 'C')),
    CONSTRAINT contrato_check_locador_inquilino_diferentes
        CHECK (id_locador <> id_inquilino)
);

-- Tabela: aluguel
CREATE TABLE IF NOT EXISTS imobly.aluguel
(
    id              BIGINT NOT NULL DEFAULT nextval('imobly.seq_aluguel_id'::regclass),
    status          CHAR(1) NOT NULL,
    id_contrato     BIGINT NOT NULL,
    ano_referencia  INT NOT NULL,
    mes_referencia  INT NOT NULL,
    data_vencimento DATE NOT NULL,
    valor_previsto  NUMERIC(10, 2) NOT NULL,
    valor_pago      NUMERIC(10, 2) NOT NULL DEFAULT 0,
    data_criacao    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    CONSTRAINT aluguel_pk PRIMARY KEY (id),
    CONSTRAINT aluguel_check_mes_referencia
        CHECK (mes_referencia BETWEEN 1 AND 12),
    CONSTRAINT aluguel_check_ano_referencia
        CHECK (ano_referencia >= 2000),
    CONSTRAINT aluguel_check_status
        CHECK (status IN ('A', 'R', 'P')),
    CONSTRAINT aluguel_check_valor_previsto
        CHECK (valor_previsto > 0),
    CONSTRAINT aluguel_check_valor_pago
        CHECK (valor_pago >= 0),
    CONSTRAINT aluguel_check_valor_pago_limite
        CHECK (valor_pago <= valor_previsto),
    CONSTRAINT aluguel_uk_contrato_competencia
        UNIQUE (id_contrato, ano_referencia, mes_referencia)
);

-- Tabela: pagamento
CREATE TABLE IF NOT EXISTS imobly.pagamento
(
    id             BIGINT NOT NULL DEFAULT nextval('imobly.seq_pagamento_id'::regclass),
    descricao      VARCHAR(255) NULL,
    valor_pagamento     NUMERIC(10, 2) NOT NULL,
    data_pagamento TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_aluguel     BIGINT NOT NULL,
    comprovante_pagamento bytea NOT NULL,
    CONSTRAINT pagamento_pk PRIMARY KEY (id),
    CONSTRAINT pagamento_check_valor_pago
        CHECK (valor_pagamento > 0)
);

-- Tabela: endereco
CREATE TABLE IF NOT EXISTS imobly.endereco
(
    id           BIGINT NOT NULL DEFAULT nextval('imobly.seq_endereco_id'::regclass),
    cep          VARCHAR(9) NOT NULL,
    logradouro   VARCHAR(255) NULL,
    numero       VARCHAR(255) NULL,
    complemento  VARCHAR(255) NULL,
    bairro       VARCHAR(255) NULL,
    cidade       VARCHAR(255) NULL,
    uf          VARCHAR(2) NULL,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_modificacao TIMESTAMP NULL,
    CONSTRAINT endereco_pk PRIMARY KEY (id),
    CONSTRAINT endereco_check_cep
        CHECK (cep ~ '^[0-9]{8}$'),
    CONSTRAINT endereco_check_uf
        CHECK (uf IN ('AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA', 'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN', 'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'))
);

-- Tabela: endereco_pessoa
CREATE TABLE IF NOT EXISTS imobly.endereco_pessoa
(
    id_pessoa BIGINT NOT NULL,
    id_endereco BIGINT NOT NULL,
    CONSTRAINT endereco_pessoa_pk PRIMARY KEY (id_pessoa, id_endereco),
    CONSTRAINT endereco_pessoa_fk_pessoa
        FOREIGN KEY (id_pessoa) REFERENCES imobly.pessoa (id),
    CONSTRAINT endereco_pessoa_fk_endereco
        FOREIGN KEY (id_endereco) REFERENCES imobly.endereco (id)
);

-- ============================================================================
-- FOREIGN KEYS
-- ============================================================================

ALTER TABLE imobly.contrato
    ADD CONSTRAINT contrato_imovel_fk
        FOREIGN KEY (id_imovel) REFERENCES imobly.imovel (id);

ALTER TABLE imobly.contrato
    ADD CONSTRAINT contrato_inquilino_fk
        FOREIGN KEY (id_inquilino) REFERENCES imobly.pessoa (id);

ALTER TABLE imobly.contrato
    ADD CONSTRAINT contrato_locador_fk
        FOREIGN KEY (id_locador) REFERENCES imobly.pessoa (id);

ALTER TABLE imobly.aluguel
    ADD CONSTRAINT aluguel_contrato_fk
        FOREIGN KEY (id_contrato) REFERENCES imobly.contrato (id);

ALTER TABLE imobly.pagamento
    ADD CONSTRAINT pagamento_aluguel_fk
        FOREIGN KEY (id_aluguel) REFERENCES imobly.aluguel (id);

ALTER TABLE imobly.imovel
    ADD CONSTRAINT imovel_endereco_fk
        FOREIGN KEY (id_endereco) REFERENCES imobly.endereco (id);

COMMENT ON COLUMN imobly.contrato.status IS 'A - ATIVO | E - ENCERRADO | C - CANCELADO';
COMMENT ON COLUMN imobly.aluguel.status IS 'A - ABERTO | R - PARCIAL | P - PAGO';