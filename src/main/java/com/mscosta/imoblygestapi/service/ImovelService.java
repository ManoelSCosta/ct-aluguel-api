package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.response.ImovelResponseDto;
import com.mscosta.imoblygestapi.entity.Imovel;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.ImovelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ImovelService {

    private static final String IMOVEL_NAO_ENCONTRADO = "Imóvel não encontrado";

    private final ImovelRepository imovelRepository;
    private final ContratoService contratoService;

    public ImovelService(ImovelRepository imovelRepository, ContratoService contratoService) {
        this.imovelRepository = imovelRepository;
        this.contratoService = contratoService;
    }

    public ImovelResponseDto getImovelById(long id) {
        return toResponseDto(loadImovelById(id));
    }

    public Page<ImovelResponseDto> getAllImoveis(ImovelRequestDto request, Pageable pageable) {
        return imovelRepository
                .findByDescricaoLikeIgnoreCaseAndEnderecoLikeIgnoreCase(
                        request.getDescricao(),
                        request.getEndereco(),
                        pageable
                )
                .map(this::toResponseDto);
    }

    public ImovelResponseDto criarImovel(ImovelRequestDto request) {
        final var imovel = new Imovel();
        imovel.setEndereco(request.getEndereco());
        imovel.setDescricao(request.getDescricao());

        imovelRepository.save(imovel);
        return toResponseDto(loadImovelById(imovel.getId()));
    }

    public ImovelResponseDto atualizarImovel(long id, ImovelRequestDto request) {
        final var imovel = loadImovelById(id);
        imovel.setEndereco(request.getEndereco());
        imovel.setDescricao(request.getDescricao());

        imovelRepository.save(imovel);
        return toResponseDto(loadImovelById(imovel.getId()));
    }

    public void deletarImovel(long id) {
        imovelRepository.deleteById(id);
    }

    private Imovel loadImovelById(long id) {
        return imovelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(IMOVEL_NAO_ENCONTRADO));
    }

    private ImovelResponseDto toResponseDto(Imovel imovel) {
        final var isDisponivel = !contratoService.existsContratoByImovelId(imovel.getId());
        return new ImovelResponseDto(imovel.getDescricao(), imovel.getEndereco(), isDisponivel);
    }
}