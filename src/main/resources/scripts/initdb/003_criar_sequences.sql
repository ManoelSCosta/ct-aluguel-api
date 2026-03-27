DROP SEQUENCE IF EXISTS imobly.seq_imovel_id;
DROP SEQUENCE IF EXISTS imobly.seq_pessoa_id;
DROP SEQUENCE IF EXISTS imobly.seq_contrato_id;
DROP SEQUENCE IF EXISTS imobly.seq_aluguel_id;
DROP SEQUENCE IF EXISTS imobly.seq_pagamento_id;
DROP SEQUENCE IF EXISTS imobly.seq_endereco_id;

CREATE SEQUENCE imobly.seq_imovel_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE imobly.seq_pessoa_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE imobly.seq_contrato_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE imobly.seq_aluguel_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE imobly.seq_pagamento_id START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE imobly.seq_endereco_id START WITH 1 INCREMENT BY 1;