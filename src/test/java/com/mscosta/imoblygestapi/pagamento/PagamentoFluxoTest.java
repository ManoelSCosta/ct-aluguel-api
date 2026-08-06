package com.mscosta.imoblygestapi.pagamento;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscosta.imoblygestapi.support.ApiFixtures;
import com.mscosta.imoblygestapi.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * O caso de uso que motivou o projeto: o inquilino paga o aluguel em parcelas
 * arbitrárias e a competência só fecha quando a soma alcança o valor previsto.
 */
@IntegrationTest
class PagamentoFluxoTest {

    private static final BigDecimal VALOR_ALUGUEL = new BigDecimal("1200.00");
    private static final LocalDate INICIO = LocalDate.of(2026, 1, 15);
    private static final LocalDate FIM = LocalDate.of(2026, 3, 15);
    private static final byte[] COMPROVANTE = "conteudo-do-comprovante".getBytes(StandardCharsets.UTF_8);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ApiFixtures fixtures;
    private long contrato;
    private long aluguel;

    @BeforeEach
    void setUp() throws Exception {
        fixtures = new ApiFixtures(mockMvc, objectMapper);

        final var locador = fixtures.criarPessoa("Locador Pagto", "10000000001", "locador.pagto@exemplo.com");
        final var inquilino = fixtures.criarPessoa("Inquilino Pagto", "10000000002", "inquilino.pagto@exemplo.com");
        final var imovel = fixtures.criarImovel("Casa do fluxo de pagamento", VALOR_ALUGUEL);

        contrato = fixtures.abrirContrato(imovel, inquilino, locador, INICIO, FIM, VALOR_ALUGUEL, 10);
        aluguel = fixtures.primeiraCompetencia(contrato);
    }

    @Test
    @DisplayName("pagamento parcial com comprovante deixa o aluguel como PARCIAL")
    void pagamentoParcialMantemAluguelEmAberto() throws Exception {
        fixtures.registrarPagamento(aluguel, "Primeira parcela", new BigDecimal("200.00"), COMPROVANTE)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.idAluguel").value(aluguel))
                .andExpect(jsonPath("$.valorPagamento").value(200.00));

        buscarAluguel()
                .andExpect(jsonPath("$[0].status").value("PARCIAL"))
                .andExpect(jsonPath("$[0].valorPago").value(200.00))
                .andExpect(jsonPath("$[0].pagamentos", hasSize(1)));
    }

    @Test
    @DisplayName("parcelas sucessivas somam até quitar a competência")
    void parcelasSucessivasQuitamOAluguel() throws Exception {
        fixtures.registrarPagamento(aluguel, "Parcela 1", new BigDecimal("200.00"), COMPROVANTE)
                .andExpect(status().isCreated());
        fixtures.registrarPagamento(aluguel, "Parcela 2", new BigDecimal("700.00"), COMPROVANTE)
                .andExpect(status().isCreated());

        buscarAluguel()
                .andExpect(jsonPath("$[0].status").value("PARCIAL"))
                .andExpect(jsonPath("$[0].valorPago").value(900.00));

        // o restante fecha o aluguel
        fixtures.registrarPagamento(aluguel, "Parcela 3", new BigDecimal("300.00"), null)
                .andExpect(status().isCreated());

        buscarAluguel()
                .andExpect(jsonPath("$[0].status").value("PAGO"))
                .andExpect(jsonPath("$[0].valorPago").value(1200.00))
                .andExpect(jsonPath("$[0].pagamentos", hasSize(3)));
    }

    @Test
    @DisplayName("pagamento sem comprovante é aceito — o comprovante é opcional")
    void pagamentoSemComprovanteEhAceito() throws Exception {
        fixtures.registrarPagamento(aluguel, "Pago em dinheiro", new BigDecimal("1200.00"), null)
                .andExpect(status().isCreated());

        buscarAluguel().andExpect(jsonPath("$[0].status").value("PAGO"));
    }

    @Test
    @DisplayName("pagamento de aluguel inexistente retorna 404")
    void pagamentoDeAluguelInexistenteRetornaNotFound() throws Exception {
        fixtures.registrarPagamento(999_999L, "Aluguel fantasma", new BigDecimal("100.00"), null)
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("valor de pagamento zerado é barrado na validação")
    void pagamentoComValorZeradoRetornaBadRequest() throws Exception {
        fixtures.registrarPagamento(aluguel, "Valor inválido", BigDecimal.ZERO, null)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.valorPagamento").exists());
    }

    private ResultActions buscarAluguel() throws Exception {
        return mockMvc.perform(get("/api/v1/aluguel/contrato/{id}", contrato))
                .andExpect(status().isOk());
    }
}
