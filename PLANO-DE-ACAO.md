# Plano de Ação — Imobly Gest API

> Execução da arquitetura definida em [ARQUITETURA.md](ARQUITETURA.md).
> Versão 1.0 — 2026-08-05.

## Como ler este plano

Cada fase entrega **algo utilizável**, não um andar de arquitetura sem uso.
A ordem é ditada por dependência técnica real, não por conforto:

- A **fase 0** é bloqueante — a aplicação não sobe com as divergências atuais.
- A **fase 1** entrega o que motivou o projeto: registrar pagamento parcelado com comprovante.
- Da **fase 2** em diante, cada fase é opcional e independente. Pode parar em qualquer ponto
  e ter um sistema coerente.

Estimativas em dias de trabalho de uma pessoa, em ritmo de projeto pessoal.

| Fase | Objetivo | Esforço | Bloqueante? |
|---|---|---|---|
| 0 | Estancar o sangramento: schema versionado e app que sobe | 2–3 d | sim |
| 1 | Pagamento parcelado com comprovante e conferência | 5–8 d | — |
| 2 | Inadimplência: vencimento, encargos, acordos | 3–4 d | — |
| 3 | Acesso do inquilino e notificações | 4–6 d | — |
| 4 | Cobrança automatizada (Pix) | 3–5 d | — |
| 5 | Relatórios e fechamento | 2–3 d | — |

---

## Fase 0 — Fundação

**Objetivo:** ter uma aplicação que sobe, com schema versionado e entidades fiéis ao banco.
Nenhuma funcionalidade nova. Sem isso, tudo o que vier depois é construído sobre erro.

### 0.1 Migração versionada

- [x] Adicionar dependência `flyway-core` + `flyway-database-postgresql` ao `pom.xml`
- [x] Converter `scripts/initdb/001–005` em `V1__baseline.sql`
- [x] Configurar `spring.flyway.schemas=imobly` (sem `baseline-on-migrate`: o schema antigo
      veio do `ddl-auto=update` e não corresponde ao `V1`; recriar é mais seguro que assumir baseline)
- [x] Fixar `spring.jpa.hibernate.ddl-auto=validate` — o Hibernate nunca mais altera schema
- [x] Remover `scripts/initdb/` da execução (manter `start-postgres.sh` para dev local)

### 0.2 Corrigir divergências entidade × DDL

Referência: seção 9.1 do ARQUITETURA.md.

- [x] `Pagamento` — `comprovante_pagamento` → `comprovante_bytea`
- [x] `Pagamento` — usar `DatabaseConstants` em vez das constantes locais
- [x] `Imovel` — `valor_aluguel` → `valor_aluguel_sugerido`
- [x] `Pessoa` — mapear `Set<Endereco>` com `@ManyToMany` sobre `endereco_pessoa`
- [x] `Pessoa` — trocar `contato` por `email` + `telefone`
- [x] `Pessoa` — mapear `nacionalidade`
- [x] `Contrato` — mapear `tipo_garantia`, `dia_vencimento`, `multa_atraso_perc`, `juros_mes_perc`
- [x] `Contrato` — `getId()`/`setId()` para `Long`
- [x] `Contrato` — remover import de `org.springframework.cglib.core.Local`
- [x] `AluguelService` — usar `contrato.diaVencimento` no lugar do dia 10 fixo

### 0.3 Enums legíveis

- [x] Renomear `StatusAluguel.A/R/P` → `ABERTO/PARCIAL/PAGO`
- [x] Renomear `StatusContrato.A/E/C` → `ATIVO/ENCERRADO/CANCELADO`
- [x] Criar `AttributeConverter` por enum, persistindo o `CHAR(1)` do DDL
- [x] Trocar `@Enumerated(STRING)` por `@Convert` nas entidades

### 0.4 Validação de entrada

- [x] Adicionar `spring-boot-starter-validation`
- [x] Anotar os request DTOs (`@NotNull`, `@Positive`, `@PastOrPresent`)
- [x] `@Valid` nos controllers
- [x] Tratar `MethodArgumentNotValidException` no `GlobalExceptionHandler`

### 0.5 Rede de segurança

- [x] `spring-boot-testcontainers` + `postgresql` no escopo de teste
- [x] Teste de contexto que sobe a app contra Postgres real e valida o schema
- [x] Um teste de integração por fluxo existente (abrir contrato, registrar pagamento)

**Critério de aceite:** `./mvnw verify` passa com Postgres real, `ddl-auto=validate`
não reclama, e abrir um contrato gera aluguéis com o dia de vencimento correto do contrato.

**Status: concluída em 2026-08-06.** `./mvnw verify` → 11 testes, 0 falhas.

Para rodar os testes com Podman em vez de Docker:

```bash
systemctl --user start podman.socket
DOCKER_HOST=unix:///run/user/1000/podman/podman.sock \
TESTCONTAINERS_RYUK_DISABLED=true \
./mvnw verify
```

Antes de subir a app local pela primeira vez, recrie o schema — o banco atual foi
criado pelo `ddl-auto=update` e o Flyway não consegue aplicar o `V1` sobre ele:

```sql
DROP SCHEMA IF EXISTS imobly CASCADE;
```

---

## Fase 1 — Pagamento parcelado com comprovante

**Objetivo:** o inquilino informa um pagamento parcial com comprovante; o locador confere e
confirma; o saldo da fatura reflete a soma das confirmações. É a entrega que resolve o
problema original.

### 1.1 Reorganizar em módulos

- [ ] Criar a estrutura de pacotes por contexto (seção 3 do ARQUITETURA.md)
- [ ] Mover as classes existentes para `cadastro/` e `locacao/`
- [ ] Reduzir a visibilidade: uma `*Facade` pública por módulo, resto `package-private`
- [ ] Extrair `shared/` (exceptions, `LoggerAspect`, `SwaggerConfig`, `DatabaseConstants`)
- [ ] `EntidadeAuditavel` com Spring Data Auditing, removendo `dataCriacao`/`dataModificacao`
      repetidos e os `setDataCriacao(now())` dos services
- [ ] ArchUnit travando as cinco regras de fronteira da seção 3.1

### 1.2 Value object Dinheiro

- [ ] `shared/domain/Dinheiro` — escala 2, `HALF_UP`, operações de soma/subtração/comparação
- [ ] Substituir `BigDecimal` cru no domínio financeiro

### 1.3 Novo modelo financeiro

- [ ] `V2__financeiro.sql`: renomear `aluguel`→`fatura`, `pagamento`→`recebimento`;
      criar `item_fatura`, `baixa`, `arquivo`; alterar `recebimento` (seção 9)
- [ ] `V3__migrar_pagamentos.sql`: para cada pagamento existente, gerar `Recebimento`
      `CONFIRMADO` + `Baixa` correspondente, preservando o histórico
- [ ] Entidades `Fatura`, `ItemFatura`, `Recebimento`, `Baixa`
- [ ] `vw_saldo_fatura` e método de recálculo do cache `valor_pago`
- [ ] `PoliticaImputacao` com as duas estratégias (`LEGAL`, `PRINCIPAL_PRIMEIRO`)

### 1.4 Armazenamento de comprovante

- [ ] Porta `ArmazenamentoArquivoPort`
- [ ] `ArmazenamentoLocalAdapter` (diretório configurável, nome por hash)
- [ ] Validação de upload: tipo (`pdf`, `jpg`, `png`), tamanho máximo, hash SHA-256
- [ ] Migrar os `bytea` existentes para arquivos e remover a coluna

### 1.5 Casos de uso

- [ ] `GerarFaturasDoContrato` — passa a reagir ao evento `ContratoAssinado`
- [ ] `InformarRecebimento` — cria `INFORMADO`, **não** credita
- [ ] `ConfirmarRecebimento` — imputa, gera baixas, recalcula status da fatura
- [ ] `RejeitarRecebimento` — motivo obrigatório
- [ ] `EstornarRecebimento` — baixas inversas, fatura reaberta
- [ ] `ConsultarPosicaoFinanceira` — saldo e faturas em aberto do contrato

### 1.6 API

- [ ] `POST /api/v1/faturas/{id}/recebimentos` (multipart)
- [ ] `GET /api/v1/recebimentos?status=INFORMADO` — fila de conferência
- [ ] `POST /api/v1/recebimentos/{id}/confirmar` | `/rejeitar` | `/estornar`
- [ ] `GET /api/v1/recebimentos/{id}/comprovante` — download
- [ ] `GET /api/v1/contratos/{id}/posicao`
- [ ] Documentar tudo no OpenAPI

### 1.7 Testes

- [ ] Invariantes I1–I7 em teste unitário
- [ ] Cenário A: 200 + 700 + 300 na mesma fatura → `QUITADA`, 3 baixas
- [ ] Cenário B: recebimento de 1.500 abatendo julho e agosto → 2 baixas
- [ ] Cenário C: rejeição não altera saldo
- [ ] Cenário D: estorno reabre fatura quitada
- [ ] Imputação com multa e juros nas duas políticas

**Critério de aceite:** o cenário narrado na seção 1 do ARQUITETURA.md — 200, promessa,
700, 300 — é executável ponta a ponta pela API, com comprovante anexado em cada etapa e
saldo correto ao final.

---

## Fase 2 — Inadimplência

**Objetivo:** o sistema sabe sozinho quem está devendo, há quanto tempo e quanto de encargo
incidiu; e registra as promessas de pagamento.

- [ ] Entidade `Acordo` + `V4__acordo.sql`
- [ ] `RegistrarAcordo` — fatura vai para `EM_ACORDO`
- [ ] `CalculadoraEncargos` — multa e juros a partir dos percentuais do contrato
- [ ] `AplicarEncargosDiarios` (`@Scheduled`): marca `VENCIDA`, cria `ItemFatura` de
      multa/juros, quebra acordos expirados
- [ ] Idempotência do job — rodar duas vezes no mesmo dia não duplica encargo
- [ ] `GET /api/v1/inadimplencia` — visão consolidada
- [ ] `GET /api/v1/pessoas/{id}/historico-acordos` — cumpridos vs. quebrados
- [ ] Teste do job com data controlada (`Clock` injetado, nunca `LocalDate.now()` direto)

**Critério de aceite:** fatura vencida há 30 dias exibe multa e juros corretos; acordo
registrado suspende a inadimplência até a data prometida e a reativa se descumprido.

---

## Fase 3 — Acesso do inquilino e notificações

**Objetivo:** o inquilino informa o próprio pagamento, sem intermediação por WhatsApp.

- [ ] `Usuario` vinculado a `Pessoa`, perfis `LOCADOR` e `INQUILINO`
- [ ] Spring Security + JWT
- [ ] Autorização por escopo: inquilino só enxerga os próprios contratos e faturas
- [ ] Confirmar/rejeitar/estornar restritos ao perfil `LOCADOR`
- [ ] Porta `NotificacaoPort` + adaptador de e-mail
- [ ] Gatilhos: recebimento informado (→ locador), confirmado/rejeitado (→ inquilino),
      fatura a vencer e vencida (→ inquilino)
- [ ] Teste de autorização: inquilino A não acessa dado do inquilino B

**Critério de aceite:** inquilino autenticado envia comprovante do próprio contrato,
locador é notificado, e a tentativa de acessar contrato alheio retorna 403.

---

## Fase 4 — Cobrança automatizada

**Objetivo:** reduzir o trabalho manual de conferência.

**Antes de integrar API bancária:** avaliar Pix estático com chave fixa + conciliação
manual. Resolve a maior parte da necessidade sem homologação de provedor. Só seguir para a
integração completa se isso comprovadamente não bastar.

- [ ] Porta `ProvedorCobrancaPort`
- [ ] Adaptador do provedor escolhido
- [ ] `POST /webhooks/{provedor}` com validação de assinatura
- [ ] Idempotência por `external_id` (índice único já criado na fase 1)
- [ ] Webhook cria `Recebimento` já `CONFIRMADO`, reutilizando `ConfirmarRecebimento`
- [ ] QR code por fatura
- [ ] Teste com payloads reais de webhook, incluindo reenvio duplicado

**Critério de aceite:** pagamento via Pix baixa a fatura automaticamente; webhook entregue
três vezes gera um único recebimento.

---

## Fase 5 — Relatórios e fechamento

- [ ] Extrato do contrato em PDF (faturas, recebimentos, saldo)
- [ ] Recibo de pagamento
- [ ] Fechamento mensal por imóvel: previsto × recebido × em aberto
- [ ] Informe de rendimentos anual para declaração de IR
- [ ] Reajuste de contrato por índice (IGP-M/IPCA) com histórico

---

## Riscos e mitigações

| Risco | Mitigação |
|---|---|
| Migração de `pagamento` → `recebimento` corromper histórico | `V3` roda em transação, com contagem e soma conferidas antes/depois; dump completo antes de aplicar |
| Refatoração de pacotes (1.1) quebrar tudo de uma vez | Executar depois da fase 0, com os testes de integração já verdes servindo de rede |
| Job de encargos duplicar lançamento | Idempotência por `(fatura, tipo, competência)` + teste explícito de reexecução |
| Escopo crescer para "produto de imobiliária" | Seção 11 do ARQUITETURA.md lista o que está fora; qualquer inclusão exige revisão do documento |
| Perda de comprovantes no storage local | Backup do diretório junto com o dump do banco desde a fase 1 |

---

## Sequência recomendada

```
Fase 0 ──> Fase 1 ──┬──> Fase 2 ──> Fase 5
                    ├──> Fase 3 ──> Fase 4
                    └──> (parar aqui já é um sistema coerente)
```

Fases 2 e 3 são independentes entre si e podem ser trocadas de ordem conforme a
necessidade real de uso.
