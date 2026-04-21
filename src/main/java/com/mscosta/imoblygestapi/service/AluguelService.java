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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AluguelService {

    private static final Logger log = LoggerFactory.getLogger(AluguelService.class);
    private final AluguelRepository aluguelRepository;

    public AluguelService(AluguelRepository aluguelRepository) {
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public void gerarAlugueis(Contrato contrato) {
        log.info("Gerando aluguéis para o contrato ID: {}", contrato.getId());

        LocalDate dataInicio = contrato.getDataInicioContrato();
        LocalDate dataFim = contrato.getDataFimContrato();

        if (dataFim == null) {
            // Se não houver data fim, gera por 12 meses como padrão
            dataFim = dataInicio.plusYears(1);
        }

        List<Aluguel> alugueis = new ArrayList<>();
        LocalDate dataAtual = dataInicio;

        while (dataAtual.isBefore(dataFim) || dataAtual.isEqual(dataFim)) {
            Aluguel aluguel = new Aluguel();
            aluguel.setContrato(contrato);
            aluguel.setAnoReferencia(dataAtual.getYear());
            aluguel.setMesReferencia(dataAtual.getMonthValue());
            aluguel.setValorPrevisto(contrato.getValorAluguel());
            aluguel.setStatus(StatusAluguel.A);
            aluguel.setDataVencimento(dataAtual.withDayOfMonth(10)); // Padrão dia 10
            aluguel.setDataCriacao(LocalDateTime.now());
            
            alugueis.add(aluguel);
            
            dataAtual = dataAtual.plusMonths(1);
        }

        aluguelRepository.saveAll(alugueis);
        log.info("Foram gerados {} aluguéis para o contrato ID: {}", alugueis.size(), contrato.getId());
    }

    public List<AluguelResponseDto> listarAlugueisPorContrato(Long contratoId) {
        log.info("Listando aluguéis para o contrato ID: {}", contratoId);
        return aluguelRepository.findAllByContratoId(contratoId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private AluguelResponseDto toResponseDto(Aluguel aluguel) {
        AluguelResponseDto response = new AluguelResponseDto();
        response.setId(aluguel.getId());
        response.setStatus(aluguel.getStatus());
        response.setAnoReferencia(aluguel.getAnoReferencia());
        response.setMesReferencia(aluguel.getMesReferencia());
        response.setDataVencimento(aluguel.getDataVencimento());
        response.setValorPrevisto(aluguel.getValorPrevisto());
        response.setValorPago(aluguel.getValorPago());
        response.setIdContrato(aluguel.getContrato().getId());
        response.setPagamentos(aluguel.getPagamentos().stream().map(p -> {
            PagamentoResponseDto pr = new PagamentoResponseDto();
            pr.setId(p.getId());
            pr.setDescricao(p.getDescricao());
            pr.setValorPagamento(p.getValorPagamento());
            pr.setDataPagamento(p.getDataPagamento());
            pr.setIdAluguel(aluguel.getId());
            return pr;
        }).toList());
        return response;
    }
}
