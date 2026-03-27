

DO
$$
DECLARE
max_id BIGINT;
BEGIN
SELECT COALESCE(MAX(id), 0)
INTO max_id
FROM imobly.imovel;
PERFORM
setval('imobly.seq_imovel_id', GREATEST(max_id, 1), false);
END $$;

DO
$$
DECLARE
max_id BIGINT;
BEGIN
SELECT COALESCE(MAX(id), 0)
INTO max_id
FROM imobly.pessoa;
PERFORM
setval('imobly.seq_pessoa_id', GREATEST(max_id, 1), false);
END $$;

DO
$$
DECLARE
max_id BIGINT;
BEGIN
SELECT COALESCE(MAX(id), 0)
INTO max_id
FROM imobly.contrato;
PERFORM
setval('imobly.seq_contrato_id', GREATEST(max_id, 1), false);
END $$;

DO
$$
DECLARE
max_id BIGINT;
BEGIN
SELECT COALESCE(MAX(id), 0)
INTO max_id
FROM imobly.aluguel;
PERFORM
setval('imobly.seq_aluguel_id', GREATEST(max_id, 1), false);
END $$;

DO
$$
DECLARE
max_id BIGINT;
BEGIN
SELECT COALESCE(MAX(id), 0)
INTO max_id
FROM imobly.pagamento;
PERFORM
setval('imobly.seq_pagamento_id', GREATEST(max_id, 1), false);
END $$;