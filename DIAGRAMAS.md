# Diagramas — Imobly Gest API

> Retrato do código **como ele está hoje**, ao final da [fase 0](PLANO-DE-ACAO.md#fase-0--fundação).
> O modelo-alvo (`Fatura`, `Recebimento`, `Baixa`) está em [ARQUITETURA.md](ARQUITETURA.md)
> e só entra na fase 1 — a seção 4 deste documento mostra a diferença.
>
> Versão 1.0 — 2026-08-06.

## Índice

1. [Diagrama de entidades (domínio persistido)](#1-diagrama-de-entidades)
2. [Diagrama de classes (camadas da aplicação)](#2-diagrama-de-classes)
3. [Diagrama de casos de uso](#3-diagrama-de-casos-de-uso)
4. [Para onde o modelo vai na fase 1](#4-para-onde-o-modelo-vai-na-fase-1)

---

## 1. Diagrama de entidades

As seis entidades JPA e os enums que as acompanham. Os enums de status são gravados
como `VARCHAR(1)` via `AttributeConverter`; `TipoGarantia` e `EstadoCivilEnum` são
gravados pelo nome da constante.

```mermaid
classDiagram
    direction LR

    class Pessoa {
        +Long id
        +String nome
        +String cpf
        +String rg
        +String nacionalidade
        +EstadoCivilEnum estadoCivil
        +String profissao
        +String email
        +String telefone
        +Set~Endereco~ enderecos
        +LocalDateTime dataCriacao
        +LocalDateTime dataModificacao
    }

    class Endereco {
        +Long id
        +String cep
        +String logradouro
        +String numero
        +String bairro
        +String cidade
        +String uf
        +String complemento
    }

    class Imovel {
        +Long id
        +String descricao
        +String matriculaRgi
        +String inscricaoIptu
        +BigDecimal valorAluguelSugerido
        +Endereco endereco
    }

    class Contrato {
        +Long id
        +LocalDate dataInicioContrato
        +LocalDate dataFimContrato
        +BigDecimal valorAluguel
        +Integer diaVencimento
        +BigDecimal multaAtrasoPerc
        +BigDecimal jurosMesPerc
        +TipoGarantia tipoGarantia
        +StatusContrato statusContrato
    }

    class Aluguel {
        +Long id
        +StatusAluguel status
        +Integer anoReferencia
        +Integer mesReferencia
        +LocalDate dataVencimento
        +BigDecimal valorPrevisto
        +BigDecimal valorPago
        +adicionarPagamento(Pagamento)
        +removerPagamento(Pagamento)
    }

    class Pagamento {
        +Long id
        +String descricao
        +BigDecimal valorPagamento
        +LocalDateTime dataPagamento
        +byte[] comprovantePagamento
    }

    class StatusContrato {
        <<enumeration>>
        ATIVO
        ENCERRADO
        CANCELADO
    }

    class StatusAluguel {
        <<enumeration>>
        ABERTO
        PARCIAL
        PAGO
    }

    class TipoGarantia {
        <<enumeration>>
        CAUCAO
        FIADOR
        SEGURO_FIANCA
        TITULO_CAPITALIZACAO
        SEM_GARANTIA
    }

    class EstadoCivilEnum {
        <<enumeration>>
        SOLTEIRO
        CASADO
        DIVORCIADO
        VIUVO
    }

    Pessoa "0..*" -- "0..*" Endereco : endereco_pessoa
    Imovel "1" *-- "1" Endereco : localizado em
    Contrato "0..*" --> "1" Imovel : objeto da locação
    Contrato "0..*" --> "1" Pessoa : inquilino
    Contrato "0..*" --> "1" Pessoa : locador
    Contrato "1" *-- "1..*" Aluguel : competências mensais
    Aluguel "1" *-- "0..*" Pagamento : parcelas recebidas

    Contrato ..> StatusContrato
    Contrato ..> TipoGarantia
    Aluguel ..> StatusAluguel
    Pessoa ..> EstadoCivilEnum
```

### Códigos gravados no banco

Os dois enums de status são persistidos como `VARCHAR(1)` por um `AttributeConverter`, então
o nome da constante em Java pode mudar sem migração de dados:

| Enum | Constante | Código no banco |
|---|---|---|
| `StatusAluguel` | `ABERTO` / `PARCIAL` / `PAGO` | `A` / `R` / `P` |
| `StatusContrato` | `ATIVO` / `ENCERRADO` / `CANCELADO` | `A` / `E` / `C` |
| `TipoGarantia` | `CAUCAO`, `FIADOR`, … | nome da constante (`VARCHAR(50)`) |
| `EstadoCivilEnum` | `SOLTEIRO`, `CASADO`, … | nome da constante (`VARCHAR(50)`) |

### O que ler nesse diagrama

- **`Pessoa` é papel, não tipo.** A mesma tabela serve locador e inquilino; quem define o
  papel é a ponta do `Contrato`. Por isso não existem entidades `Locador`/`Inquilino`.
- **`Aluguel` é uma competência mensal**, não um pagamento. Um contrato de 12 meses gera
  12 aluguéis na abertura, cada um com `anoReferencia`/`mesReferencia` e vencimento no
  `diaVencimento` do contrato.
- **`valorPago` é um cache**, mantido pelo `PagamentoService` a cada pagamento. O valor
  verdadeiro é a soma dos `Pagamento` — divergência entre os dois é bug silencioso, e é um
  dos motivos da reforma da fase 1.
- **`Pagamento` aponta para exatamente um `Aluguel`.** É aqui que o modelo atual trava: um
  pagamento de R$ 1.500 que quita dezembro e adianta janeiro não tem como ser representado.

---

## 2. Diagrama de classes

Camadas da aplicação. A direção das setas é a direção da dependência — nenhuma seta sobe.

```mermaid
classDiagram
    direction TB

    class PessoaController {
        +cadastrarPessoa(PessoaRequestDto)
        +getPessoaById(long)
        +atualizarPessoa(long, PessoaRequestDto)
        +deletarPessoa(long)
    }
    class ImovelController {
        +criarImovel(ImovelRequestDto)
        +getImovelById(long)
        +getAllImoveis(ImovelFiltroDto)
        +atualizarImovel(long, ImovelRequestDto)
        +deletarImovel(long)
    }
    class ContratoController {
        +abrirContrato(ContratoRequestDto)
        +listarContratosAtivos()
        +getContratoById(long)
    }
    class AluguelController {
        +listarAlugueisPorContrato(Long)
    }
    class PagamentoController {
        +registrarPagamento(PagamentoRequestDto, MultipartFile)
    }

    class PessoaService {
        +cadastrarPessoa()
        +atualizarPessoa()
        +deletarPessoa()
    }
    class ImovelService {
        +criarImovel()
        +buscarImoveis()
        +atualizarImovel()
    }
    class ContratoService {
        +abrirContrato()
        +listarContratosAtivos()
        -validarPartes()
    }
    class AluguelService {
        +gerarAlugueis(Contrato)
        +listarAlugueisPorContrato(Long)
        -novoAluguel(Contrato, LocalDate)
    }
    class PagamentoService {
        +registrarPagamento(request, comprovante)
    }

    class PessoaRepository {
        <<interface>>
    }
    class ImovelRepository {
        <<interface>>
        +buscarPorDescricaoOuLogradouro()
    }
    class ContratoRepository {
        <<interface>>
        +findAllByStatusContrato()
        +existsByImovelIdAndStatusContrato()
    }
    class AluguelRepository {
        <<interface>>
        +findAllByContratoIdOrderByAnoReferenciaAscMesReferenciaAsc()
    }
    class PagamentoRepository {
        <<interface>>
    }

    class GlobalExceptionHandler {
        <<RestControllerAdvice>>
        +handleNotFound() 404
        +handleBusiness() 422
        +handleValidation() 400
        +handleDataIntegrity() 409
    }
    class LoggerAspect {
        <<aspect>>
        +logMethodCall()
        +logException()
    }

    PessoaController --> PessoaService
    ImovelController --> ImovelService
    ContratoController --> ContratoService
    AluguelController --> AluguelService
    PagamentoController --> PagamentoService

    ContratoService --> AluguelService : gera competências

    PessoaService --> PessoaRepository
    ImovelService --> ImovelRepository
    ImovelService --> ContratoService : imóvel disponível?
    ContratoService --> ContratoRepository
    ContratoService --> ImovelRepository
    ContratoService --> PessoaRepository
    AluguelService --> AluguelRepository
    PagamentoService --> PagamentoRepository
    PagamentoService --> AluguelRepository : atualiza saldo

    GlobalExceptionHandler ..> PessoaController : trata exceções de todos
    LoggerAspect ..> ContratoService : @Around em controller e service
```

**Onde as regras de negócio moram hoje:** nos services. `ContratoService.validarPartes()`
recusa imóvel já locado e locador igual ao inquilino; `AluguelService.novoAluguel()` decide
o vencimento; `PagamentoService.registrarPagamento()` decide o status do aluguel. A fase 1
move essas decisões para dentro das entidades.

---

## 3. Diagrama de casos de uso

Mermaid não tem notação UML de caso de uso, então os casos aparecem como elipses e os
atores como retângulos — a leitura é a mesma. **Linha cheia = implementado.
Linha tracejada = previsto no plano de ação.**

```mermaid
flowchart LR
    locador[👤 Locador<br/>administra os imóveis]
    inquilino[👤 Inquilino<br/>paga o aluguel]
    agendador[⚙️ Agendador<br/>ator sistema]

    subgraph sistema["Sistema Imobly"]
        direction TB

        subgraph cadastro["Cadastro"]
            uc1(["Cadastrar pessoa"])
            uc2(["Cadastrar imóvel"])
            uc3(["Consultar imóveis<br/>com filtro"])
        end

        subgraph locacao["Locação"]
            uc4(["Abrir contrato"])
            uc5(["Gerar competências<br/>mensais"])
            uc6(["Listar contratos ativos"])
            uc7(["Consultar aluguéis<br/>do contrato"])
        end

        subgraph financeiro["Financeiro"]
            uc8(["Registrar pagamento<br/>parcial"])
            uc9(["Anexar comprovante"])
            uc10(["Atualizar saldo<br/>e status do aluguel"])
        end

        subgraph futuro["Previsto — fases 1 a 5"]
            uc11(["Conferir e confirmar<br/>recebimento"])
            uc12(["Consultar meu débito"])
            uc13(["Calcular multa e juros"])
            uc14(["Registrar acordo<br/>de parcelamento"])
            uc15(["Notificar vencimento"])
            uc16(["Cobrar via Pix"])
            uc17(["Emitir recibo e<br/>relatório anual"])
        end
    end

    locador --> uc1
    locador --> uc2
    locador --> uc3
    locador --> uc4
    locador --> uc6
    locador --> uc7
    locador --> uc8

    inquilino -.-> uc12
    inquilino -.-> uc8

    locador -.-> uc11
    locador -.-> uc14
    locador -.-> uc17
    agendador -.-> uc13
    agendador -.-> uc15
    agendador -.-> uc16

    uc4 -->|include| uc5
    uc8 -->|include| uc10
    uc8 -->|extend| uc9
    uc11 -.->|include| uc10
    uc13 -.->|extend| uc10

    classDef feito fill:#dff5e1,stroke:#2f7a45,color:#14361f
    classDef previsto fill:#f2f2f2,stroke:#999,color:#444,stroke-dasharray:4
    class uc1,uc2,uc3,uc4,uc5,uc6,uc7,uc8,uc9,uc10 feito
    class uc11,uc12,uc13,uc14,uc15,uc16,uc17 previsto
```

### Atores

| Ator | Quem é na prática | Acesso hoje |
|---|---|---|
| **Locador** | seus pais / você administrando | único usuário da API — sem autenticação ainda |
| **Inquilino** | quem aluga a casa | nenhum; informa o pagamento pelo WhatsApp e o locador registra |
| **Agendador** | rotina automática (`@Scheduled`) | não existe; entra na fase 2 |

### Casos de uso implementados

| # | Caso de uso | Endpoint | Regras já aplicadas |
|---|---|---|---|
| 1 | Cadastrar pessoa | `POST /api/v1/pessoa` | CPF e e-mail únicos e validados |
| 2 | Cadastrar imóvel | `POST /api/v1/imovel` | endereço obrigatório, CEP e UF validados |
| 3 | Consultar imóveis | `GET /api/v1/imovel` | filtro por descrição/logradouro, paginado |
| 4 | Abrir contrato | `POST /api/v1/contrato` | recusa imóvel já locado e locador = inquilino |
| 5 | Gerar competências | (interno, no `AluguelService`) | uma por mês da vigência, vencendo no `diaVencimento` |
| 6 | Listar contratos ativos | `GET /api/v1/contrato` | só `StatusContrato.ATIVO` |
| 7 | Consultar aluguéis | `GET /api/v1/aluguel/contrato/{id}` | ordenados por competência |
| 8 | Registrar pagamento | `POST /api/v1/pagamento` | multipart; valor mínimo 0,01; data não futura |
| 9 | Anexar comprovante | mesma chamada, parte `comprovante` | opcional (pagamento em dinheiro) |
| 10 | Atualizar saldo | (interno, no `PagamentoService`) | soma → `PARCIAL` ou `PAGO` |

**O fluxo que motivou o projeto** é a sequência 8 → 10 repetida: João deve R$ 1.200, paga
R$ 200 (`PARCIAL`), depois R$ 700 (`PARCIAL`), depois R$ 300 (`PAGO`). É exatamente o que o
`PagamentoFluxoTest` verifica.

---

## 4. Para onde o modelo vai na fase 1

O diagrama da seção 1 tem uma limitação estrutural: `Pagamento → Aluguel` é N:1, então um
pagamento pertence a um único mês. A fase 1 quebra essa amarra com uma tabela de alocação.

```mermaid
classDiagram
    direction LR

    class Fatura {
        +competência
        +vencimento
        +StatusFatura status
    }
    class ItemFatura {
        +descrição
        +valor
        +TipoItem tipo
    }
    class Recebimento {
        +valor
        +data
        +StatusRecebimento status
        +Arquivo comprovante
    }
    class Baixa {
        +valor alocado
    }

    Fatura "1" *-- "1..*" ItemFatura : aluguel, multa, juros
    Fatura "1" -- "0..*" Baixa
    Recebimento "1" -- "1..*" Baixa
    note for Baixa "Um recebimento pode quitar várias faturas, e uma fatura pode ser quitada por vários recebimentos"
```

Duas mudanças de comportamento vêm junto:

1. **Saldo derivado.** O `valorPago` deixa de ser campo gravado e passa a ser a soma das
   baixas (`vw_saldo_fatura`) — acaba a possibilidade de o cache divergir.
2. **Recebimento tem estado.** Hoje, informar um pagamento já abate a dívida na hora, mesmo
   sem ninguém conferir o comprovante. Passa a existir `INFORMADO → CONFIRMADO`, e só o
   confirmado gera baixa. É o mesmo ponto onde o webhook do Pix vai desembocar na fase 4.

Detalhamento completo — DDL, máquinas de estado e política de imputação — na seção 6 do
[ARQUITETURA.md](ARQUITETURA.md).
