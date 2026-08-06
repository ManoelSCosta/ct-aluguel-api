package com.mscosta.imoblygestapi.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.request.EnderecoRequestDto;
import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.request.PagamentoRequestDto;
import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.enums.TipoGarantia;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Cria via API os cadastros que os fluxos de contrato e pagamento pressupõem.
 * Passar pelos endpoints (em vez de inserir direto no banco) mantém os testes
 * sensíveis a quebras de validação e de serialização.
 */
public class ApiFixtures {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ApiFixtures(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public long criarPessoa(String nome, String cpf, String email) throws Exception {
        final var request = new PessoaRequestDto(nome, cpf, email, "21999990000",
                "MG1234567", null, null, "Autônomo");

        final var json = mockMvc.perform(post("/api/v1/pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(json).get("id").asLong();
    }

    public long criarImovel(String descricao, BigDecimal valorSugerido) throws Exception {
        final var endereco = new EnderecoRequestDto("20000000", "Rua das Casas", "100",
                "Centro", "Rio de Janeiro", "RJ", null);
        final var request = new ImovelRequestDto(descricao, endereco, valorSugerido, null, null);

        final var json = mockMvc.perform(post("/api/v1/imovel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(json).get("id").asLong();
    }

    public long abrirContrato(long idImovel, long idInquilino, long idLocador,
                              LocalDate inicio, LocalDate fim,
                              BigDecimal valorAluguel, Integer diaVencimento) throws Exception {
        final var request = new ContratoRequestDto(idImovel, idInquilino, idLocador, inicio, fim,
                valorAluguel, TipoGarantia.SEM_GARANTIA, diaVencimento, null, null);

        final var json = mockMvc.perform(post("/api/v1/contrato")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(json).get("id").asLong();
    }

    /**
     * Devolve o id da primeira competência gerada pelo contrato — é sobre ela que
     * os testes de pagamento parcelado trabalham.
     */
    public long primeiraCompetencia(long idContrato) throws Exception {
        final var json = mockMvc.perform(get("/api/v1/aluguel/contrato/{id}", idContrato))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(json).get(0).get("id").asLong();
    }

    /**
     * Registra um pagamento no formato que o app usa de verdade: multipart com a
     * parte JSON e, opcionalmente, o comprovante enviado pelo inquilino.
     */
    public ResultActions registrarPagamento(long idAluguel, String descricao, BigDecimal valor,
                                            byte[] comprovante) throws Exception {
        final var request = new PagamentoRequestDto(descricao, valor,
                LocalDateTime.now().minusMinutes(1), idAluguel);

        final var partePagamento = new MockMultipartFile("pagamento", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request));

        final var chamada = multipart("/api/v1/pagamento").file(partePagamento);
        if (comprovante != null) {
            chamada.file(new MockMultipartFile("comprovante", "comprovante.png",
                    MediaType.IMAGE_PNG_VALUE, comprovante));
        }

        return mockMvc.perform(chamada);
    }
}
