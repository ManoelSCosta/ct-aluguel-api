package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.response.ContratoResponseDto;
import com.mscosta.imoblygestapi.entity.Contrato;
import com.mscosta.imoblygestapi.enums.StatusContrato;
import com.mscosta.imoblygestapi.exception.BusinessException;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.ContratoRepository;
import com.mscosta.imoblygestapi.repository.ImovelRepository;
import com.mscosta.imoblygestapi.repository.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContratoService {

    private static final Logger log = LoggerFactory.getLogger(ContratoService.class);
    private static final String CONTRATO_NAO_ENCONTRADO = "Contrato não encontrado";
    private static final String IMOVEL_NAO_ENCONTRADO = "Imóvel não encontrado";
    private static final String PESSOA_NAO_ENCONTRADA = "Pessoa não encontrada";
    private static final String IMOVEL_JA_LOCADO = "Imóvel já possui contrato ativo";
    private static final String LOCADOR_IGUAL_INQUILINO = "Locador e inquilino devem ser pessoas distintas";

    private final ContratoRepository contratoRepository;
    private final ImovelRepository imovelRepository;
    private final PessoaRepository pessoaRepository;
    private final AluguelService aluguelService;

    public ContratoService(ContratoRepository contratoRepository,
                           ImovelRepository imovelRepository,
                           PessoaRepository pessoaRepository,
                           AluguelService aluguelService) {
        this.contratoRepository = contratoRepository;
        this.imovelRepository = imovelRepository;
        this.pessoaRepository = pessoaRepository;
        this.aluguelService = aluguelService;
    }

    @Transactional
    public ContratoResponseDto abrirContrato(ContratoRequestDto request) {
        log.info("Abrindo novo contrato para imóvel ID: {}", request.idImovel());

        validarPartes(request);

        final var imovel = imovelRepository.findById(request.idImovel())
                .orElseThrow(() -> new NotFoundException(IMOVEL_NAO_ENCONTRADO));

        final var inquilino = pessoaRepository.findById(request.idInquilino())
                .orElseThrow(() -> new NotFoundException(PESSOA_NAO_ENCONTRADA));

        final var locador = pessoaRepository.findById(request.idLocador())
                .orElseThrow(() -> new NotFoundException(PESSOA_NAO_ENCONTRADA));

        final var contrato = new Contrato();
        contrato.setImovel(imovel);
        contrato.setInquilino(inquilino);
        contrato.setLocador(locador);
        contrato.setDataInicioContrato(request.dataInicioContrato());
        contrato.setDataFimContrato(request.dataFimContrato());
        contrato.setValorAluguel(request.valorAluguel());
        contrato.setTipoGarantia(request.tipoGarantia());
        contrato.setStatusContrato(StatusContrato.ATIVO);

        // Nulos aqui significam "usar o padrão da entidade", não "limpar o valor".
        if (request.diaVencimento() != null) {
            contrato.setDiaVencimento(request.diaVencimento());
        }
        if (request.multaAtrasoPerc() != null) {
            contrato.setMultaAtrasoPerc(request.multaAtrasoPerc());
        }
        if (request.jurosMesPerc() != null) {
            contrato.setJurosMesPerc(request.jurosMesPerc());
        }

        final var savedContrato = contratoRepository.save(contrato);
        log.info("Contrato aberto com sucesso. ID: {}", savedContrato.getId());

        aluguelService.gerarAlugueis(savedContrato);

        return toResponseDto(savedContrato);
    }

    @Transactional(readOnly = true)
    public List<ContratoResponseDto> listarContratosAtivos() {
        log.info("Listando todos os contratos ativos");
        return contratoRepository.findAllByStatusContrato(StatusContrato.ATIVO)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContratoResponseDto getContratoById(long id) {
        log.info("Buscando contrato por ID: {}", id);
        return toResponseDto(findContratoById(id));
    }

    @Transactional(readOnly = true)
    public boolean existsContratoByImovelId(long imovelId) {
        return contratoRepository.existsByImovelIdAndStatusContrato(imovelId, StatusContrato.ATIVO);
    }

    private void validarPartes(ContratoRequestDto request) {
        if (request.idLocador().equals(request.idInquilino())) {
            throw new BusinessException(LOCADOR_IGUAL_INQUILINO);
        }
        if (existsContratoByImovelId(request.idImovel())) {
            throw new BusinessException(IMOVEL_JA_LOCADO);
        }
    }

    private Contrato findContratoById(long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CONTRATO_NAO_ENCONTRADO));
    }

    private ContratoResponseDto toResponseDto(Contrato contrato) {
        final var response = new ContratoResponseDto();
        response.setId(contrato.getId());
        response.setDataCriacao(contrato.getDataCriacao());
        response.setDataInicioContrato(contrato.getDataInicioContrato());
        response.setDataFimContrato(contrato.getDataFimContrato());
        response.setValorAluguel(contrato.getValorAluguel());
        response.setStatusContrato(contrato.getStatusContrato());
        response.setNomeInquilino(contrato.getInquilino().getNome());
        response.setNomeLocador(contrato.getLocador().getNome());
        response.setDescricaoImovel(contrato.getImovel().getDescricao());
        return response;
    }
}
