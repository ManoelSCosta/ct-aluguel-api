package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.PagamentoRequestDto;
import com.mscosta.imoblygestapi.dto.response.PagamentoResponseDto;
import com.mscosta.imoblygestapi.entity.Aluguel;
import com.mscosta.imoblygestapi.entity.Pagamento;
import com.mscosta.imoblygestapi.enums.StatusAluguel;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.AluguelRepository;
import com.mscosta.imoblygestapi.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PagamentoService {

    private static final Logger log = LoggerFactory.getLogger(PagamentoService.class);
    private static final String ALUGUEL_NAO_ENCONTRADO = "Aluguel não encontrado";

    private final PagamentoRepository pagamentoRepository;
    private final AluguelRepository aluguelRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, AluguelRepository aluguelRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    @Transactional
    public PagamentoResponseDto registrarPagamento(PagamentoRequestDto request, byte[] comprovante) {
        log.info("Registrando pagamento para o aluguel ID: {}", request.idAluguel());

        Aluguel aluguel = aluguelRepository.findById(request.idAluguel())
                .orElseThrow(() -> new NotFoundException(ALUGUEL_NAO_ENCONTRADO));

        Pagamento pagamento = new Pagamento(
                null,
                request.descricao(),
                request.valorPagamento(),
                request.dataPagamento(),
                comprovante,
                aluguel
        );

        final var savedPagamento = pagamentoRepository.save(pagamento);

        // Atualizar o valor pago no aluguel
        BigDecimal novoValorPagoTotal = aluguel.getValorPago().add(request.valorPagamento());
        aluguel.setValorPago(novoValorPagoTotal);

        // Atualizar o status do aluguel
        if (novoValorPagoTotal.compareTo(aluguel.getValorPrevisto()) >= 0) {
            aluguel.setStatus(StatusAluguel.PAGO);
        } else if (novoValorPagoTotal.compareTo(BigDecimal.ZERO) > 0) {
            aluguel.setStatus(StatusAluguel.PARCIAL);
        }

        aluguelRepository.save(aluguel);

        log.info("Pagamento registrado com sucesso. ID: {}", savedPagamento.getId());
        return toResponseDto(savedPagamento);
    }

    private PagamentoResponseDto toResponseDto(Pagamento pagamento) {
        PagamentoResponseDto response = new PagamentoResponseDto();
        response.setId(pagamento.getId());
        response.setDescricao(pagamento.getDescricao());
        response.setValorPagamento(pagamento.getValorPagamento());
        response.setDataPagamento(pagamento.getDataPagamento());
        response.setIdAluguel(pagamento.getAluguel().getId());
        return response;
    }
}
