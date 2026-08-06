package com.mscosta.imoblygestapi.support;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Sobe um PostgreSQL real para os testes. Banco em memória não serve aqui: o
 * schema usa sequences por schema, checks com regex e BYTEA — nada disso se
 * comporta igual em H2.
 *
 * <p>O container é reaproveitado entre as classes de teste porque o Spring
 * cacheia o contexto que o declara.
 */
@TestConfiguration(proxyBeanMethods = false)
public class PostgresContainerConfig {

    private static final String IMAGEM_POSTGRES = "postgres:16-alpine";

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(IMAGEM_POSTGRES)
                .withDatabaseName("misuradb")
                .withUsername("mscosta")
                .withPassword("mscosta");
    }
}
