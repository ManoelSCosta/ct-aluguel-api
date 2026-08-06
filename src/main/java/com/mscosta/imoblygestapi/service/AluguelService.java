package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.response.AluguelResponseDto;
import com.mscosta.imoblygestapi.dto.response.PagamentoResponseDto;
import com.mscosta.imoblygestapi.entity.Aluguel;
import com.mscosta.imoblygestapi.entity.Contrato;
import com.mscosta.imoblygestapi.enums.StatusAluguel;
import com.mscosta.imoblygestapi.repository.AluguelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AluguelService {

    private static final Logger log = LoggerFactory.getLogger(AluguelService.class);
    private static final int MESES_PADRAO_SEM_DATA_FIM = 12;

    private final AluguelRepository aluguelRepository;

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    /**
     * Cria uma competência mensal para cada mês da vigência do contrato.
     * O vencimento usa o dia configurado no contrato (limitado a 1..28 pelo banco,
     * justamente para que todo mês tenha esse dia).
     */
    @Transactional
    public void gerarAlugueis(Contrato contrato) {
        log.info("Gerando aluguéis para o contrato ID: {}", contrato.getId());

        final var dataInicio = contrato.getDataInicioContrato();
        final var dataFim = contrato.getDataFimContrato() != null
                ? contrato.getDataFimContrato()
                : dataInicio.plusMonths(MESES_PADRAO_SEM_DATA_FIM);

        final List<Aluguel> alugueis = new ArrayList<>();
        var competencia = dataInicio.withDayOfMonth(1);
        final var ultimaCompetencia = dataFim.withDayOfMonth(1);

        while (!competencia.isAfter(ultimaCompetencia)) {
            alugueis.add(novoAluguel(contrato, competencia));
            competencia = competencia.plusMonths(1);
        }

        aluguelRepository.saveAll(alugueis);
        log.info("Foram gerados {} aluguéis para o contrato ID: {}", alugueis.size(), contrato.getId());
    }

    @Transactional(readOnly = true)
    public List<AluguelResponseDto> listarAlugueisPorContrato(Long contratoId) {
        log.info("Listando aluguéis para o contrato ID: {}", contratoId);
        return aluguelRepository.findAllByContratoIdOrderByAnoReferenciaAscMesReferenciaAsc(contratoId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private Aluguel novoAluguel(Contrato contrato, LocalDate competencia) {
        final var aluguel = new Aluguel();
        aluguel.setContrato(contrato);
        aluguel.setAnoReferencia(competencia.getYear());
        aluguel.setMesReferencia(competencia.getMonthValue());
        aluguel.setValorPrevisto(contrato.getValorAluguel());
        aluguel.setStatus(StatusAluguel.ABERTO);
        aluguel.setDataVencimento(competencia.withDayOfMonth(contrato.getDiaVencimento()));
        return aluguel;
    }

    private AluguelResponseDto toResponseDto(Aluguel aluguel) {
        final var response = new AluguelResponseDto();
        response.setId(aluguel.getId());
        response.setStatus(aluguel.getStatus());
        response.setAnoReferencia(aluguel.getAnoReferencia());
        response.setMesReferencia(aluguel.getMesReferencia());
        response.setDataVencimento(aluguel.getDataVencimento());
        response.setValorPrevisto(aluguel.getValorPrevisto());
        response.setValorPago(aluguel.getValorPago());
        response.setIdContrato(aluguel.getContrato().getId());
        response.setPagamentos(aluguel.getPagamentos().stream().map(pagamento -> {
            final var dto = new PagamentoResponseDto();
            dto.setId(pagamento.getId());
            dto.setDescricao(pagamento.getDescricao());
            dto.setValorPagamento(pagamento.getValorPagamento());
            dto.setDataPagamento(pagamento.getDataPagamento());
            dto.setIdAluguel(aluguel.getId());
            return dto;
        }).toList());
        return response;
    }
}
