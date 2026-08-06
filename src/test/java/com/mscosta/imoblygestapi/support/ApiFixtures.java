package com.mscosta.imoblygestapi.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.request.EnderecoRequestDto;
import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.enums.TipoGarantia;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
