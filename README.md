# Imobly-Gest-API

API REST para gestao de alugueis de imoveis, com foco no cadastro e controle de imoveis, inquilinos, contratos e pagamentos.

## Visao geral

O projeto foi construido com Spring Boot para centralizar operacoes comuns de uma administracao de locacoes, oferecendo uma base backend para registro e acompanhamento das principais entidades do dominio.

## Funcionalidades

- Cadastro e gerenciamento de imoveis
- Cadastro e historico de inquilinos
- Criacao, renovacao e cancelamento de contratos
- Registro de pagamentos e acompanhamento de inadimplencia
- Estrutura preparada para evolucao da documentacao da API

## Tecnologias

- Java 25
- Spring Boot 3.5.0
- Spring Data JPA
- PostgreSQL
- Maven
- Springdoc OpenAPI
- Spring REST Docs

## Pre-requisitos

- JDK 25
- PostgreSQL configurado
- Maven Wrapper (`./mvnw`) ou Maven instalado

## Como executar

1. Configure o banco de dados PostgreSQL e as variaveis/propriedades da aplicacao.
2. Inicie a aplicacao:

```bash
./mvnw spring-boot:run
```

## Testes

```bash
./mvnw test
```

## Documentacao da API

Com a aplicacao em execucao, a interface do OpenAPI pode ser acessada em:

```text
/swagger-ui/index.html
```

## Contribuicao

Contribuicoes sao bem-vindas. Antes de abrir alteracoes, mantenha a documentacao e os testes alinhados com o comportamento esperado da API.

## Licenca

Este projeto esta licenciado sob os termos descritos em [LICENSE](LICENSE).
