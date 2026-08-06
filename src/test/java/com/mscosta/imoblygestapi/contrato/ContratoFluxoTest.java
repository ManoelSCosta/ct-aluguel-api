package com.mscosta.imoblygestapi.contrato;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.enums.TipoGarantia;
import com.mscosta.imoblygestapi.support.ApiFixtures;
import com.mscosta.imoblygestapi.support.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
class ContratoFluxoTest {

    private static final BigDecimal VALOR_ALUGUEL = new BigDecimal("1200.00");
    private static final LocalDate INICIO = LocalDate.of(2026, 1, 15);
    private static final LocalDate FIM = LocalDate.of(2026, 12, 15);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ApiFixtures fixtures;

    @BeforeEach
    void setUp() {
        fixtures = new ApiFixtures(mockMvc, objectMapper);
    }

    @Test
    @DisplayName("abrir contrato gera uma competência por mês, vencendo no dia configurado")
    void abrirContratoGeraAlugueisComVencimentoDoContrato() throws Exception {
        final var locador = fixtures.criarPessoa("Maria Locadora", "11111111111", "maria@exemplo.com");
        final var inquilino = fixtures.criarPessoa("João Inquilino", "22222222222", "joao@exemplo.com");
        final var imovel = fixtures.criarImovel("Casa 1 - fundos", VALOR_ALUGUEL);

        final var contrato = fixtures.abrirContrato(imovel, inquilino, locador, INICIO, FIM, VALOR_ALUGUEL, 10);

        mockMvc.perform(get("/api/v1/aluguel/contrato/{id}", contrato))
                .andExpect(status().isOk())
                // janeiro a dezembro de 2026
                .andExpect(jsonPath("$", hasSize(12)))
                .andExpect(jsonPath("$[0].anoReferencia").value(2026))
                .andExpect(jsonPath("$[0].mesReferencia").value(1))
                // o dia vem do contrato, não de um valor fixo no serviço
                .andExpect(jsonPath("$[0].dataVencimento").value("2026-01-10"))
                .andExpect(jsonPath("$[0].status").value("ABERTO"))
                .andExpect(jsonPath("$[0].valorPrevisto").value(1200.00))
                .andExpect(jsonPath("$[0].valorPago").value(0));
    }

    @Test
    @DisplayName("sem dia de vencimento informado, vale o padrão da entidade")
    void abrirContratoSemDiaVencimentoUsaPadrao() throws Exception {
        final var locador = fixtures.criarPessoa("Ana Locadora", "33333333333", "ana@exemplo.com");
        final var inquilino = fixtures.criarPessoa("Beto Inquilino", "44444444444", "beto@exemplo.com");
        final var imovel = fixtures.criarImovel("Casa 2", VALOR_ALUGUEL);

        final var contrato = fixtures.abrirContrato(imovel, inquilino, locador, INICIO, FIM, VALOR_ALUGUEL, null);

        mockMvc.perform(get("/api/v1/aluguel/contrato/{id}", contrato))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dataVencimento").value("2026-01-05"));
    }

    @Test
    @DisplayName("imóvel com contrato ativo não aceita um segundo contrato")
    void imovelJaLocadoRejeitaNovoContrato() throws Exception {
        final var locador = fixtures.criarPessoa("Carlos Locador", "55555555555", "carlos@exemplo.com");
        final var inquilino = fixtures.criarPessoa("Dora Inquilina", "66666666666", "dora@exemplo.com");
        final var outroInquilino = fixtures.criarPessoa("Elias", "77777777777", "elias@exemplo.com");
        final var imovel = fixtures.criarImovel("Casa 3", VALOR_ALUGUEL);

        fixtures.abrirContrato(imovel, inquilino, locador, INICIO, FIM, VALOR_ALUGUEL, 10);

        final var segundo = new ContratoRequestDto(imovel, outroInquilino, locador, INICIO, FIM,
                VALOR_ALUGUEL, TipoGarantia.SEM_GARANTIA, 10, null, null);

        mockMvc.perform(post("/api/v1/contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segundo)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("contrato sem tipo de garantia é rejeitado na validação, não no banco")
    void contratoSemGarantiaRetornaBadRequest() throws Exception {
        final var locador = fixtures.criarPessoa("Fabio Locador", "88888888888", "fabio@exemplo.com");
        final var inquilino = fixtures.criarPessoa("Gina Inquilina", "99999999999", "gina@exemplo.com");
        final var imovel = fixtures.criarImovel("Casa 4", VALOR_ALUGUEL);

        final var request = new ContratoRequestDto(imovel, inquilino, locador, INICIO, FIM,
                VALOR_ALUGUEL, null, 10, null, null);

        mockMvc.perform(post("/api/v1/contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.tipoGarantia").exists());
    }

    @Test
    @DisplayName("pessoa com CPF inválido é rejeitada com detalhe do campo")
    void pessoaComCpfInvalidoRetornaBadRequest() throws Exception {
        final var request = new PessoaRequestDto("Sem CPF", "123", "semcpf@exemplo.com",
                null, null, null, null, null);

        mockMvc.perform(post("/api/v1/pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.cpf").exists());
    }
}
