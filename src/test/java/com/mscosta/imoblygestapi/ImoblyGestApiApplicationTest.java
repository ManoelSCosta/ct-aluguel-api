package com.mscosta.imoblygestapi;

import com.mscosta.imoblygestapi.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Com ddl-auto=validate, subir o contexto contra um Postgres real já prova que
 * toda entidade JPA corresponde ao schema criado pelo Flyway. É o teste que
 * pega divergência de nome de coluna antes de ela virar erro em produção.
 */
@IntegrationTest
class ImoblyGestApiApplicationTest {

    @Test
    @DisplayName("contexto sobe e o schema do Flyway valida contra as entidades JPA")
    void contextLoads() {
        // sem asserção: a falha se manifesta como erro de inicialização do contexto
    }
}
