package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.EnderecoRequestDto;
import com.mscosta.imoblygestapi.dto.request.ImovelFiltroDto;
import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.response.ImovelResponseDto;
import com.mscosta.imoblygestapi.entity.Endereco;
import com.mscosta.imoblygestapi.entity.Imovel;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.ImovelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImovelService {

    private static final String IMOVEL_NAO_ENCONTRADO = "Imóvel não encontrado";

    private final ImovelRepository imovelRepository;
    private final ContratoService contratoService;

    public ImovelService(ImovelRepository imovelRepository, ContratoService contratoService) {
        this.imovelRepository = imovelRepository;
        this.contratoService = contratoService;
    }

    @Transactional(readOnly = true)
    public ImovelResponseDto getImovelById(long id) {
        return toResponseDto(loadImovelById(id));
    }

    @Transactional(readOnly = true)
    public Page<ImovelResponseDto> getAllImoveis(ImovelFiltroDto filtro, Pageable pageable) {
        return imovelRepository
                .buscarPorDescricaoOuLogradouro(filtro.descricao(), filtro.logradouro(), pageable)
                .map(this::toResponseDto);
    }

    @Transactional
    public ImovelResponseDto criarImovel(ImovelRequestDto request) {
        final var imovel = new Imovel();
        aplicarDados(imovel, request);
        return toResponseDto(imovelRepository.save(imovel));
    }

    @Transactional
    public ImovelResponseDto atualizarImovel(long id, ImovelRequestDto request) {
        final var imovel = loadImovelById(id);
        aplicarDados(imovel, request);
        return toResponseDto(imovelRepository.save(imovel));
    }

    @Transactional
    public void deletarImovel(long id) {
        imovelRepository.deleteById(id);
    }

    private Imovel loadImovelById(long id) {
        return imovelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(IMOVEL_NAO_ENCONTRADO));
    }

    private void aplicarDados(Imovel imovel, ImovelRequestDto request) {
        imovel.setDescricao(request.descricao());
        imovel.setValorAluguelSugerido(request.valorAluguelSugerido());
        imovel.setMatriculaRgi(request.matriculaRgi());
        imovel.setInscricaoIptu(request.inscricaoIptu());
        aplicarEndereco(imovel, request.endereco());
    }

    /**
     * Reaproveita o endereço já associado ao imóvel: substituir a instância deixaria
     * o registro anterior órfão na tabela endereco.
     */
    private void aplicarEndereco(Imovel imovel, EnderecoRequestDto dto) {
        if (dto == null) {
            return;
        }
        final var endereco = imovel.getEndereco() != null ? imovel.getEndereco() : new Endereco();
        endereco.setCep(dto.cep());
        endereco.setLogradouro(dto.logradouro());
        endereco.setNumero(dto.numero());
        endereco.setBairro(dto.bairro());
        endereco.setCidade(dto.cidade());
        endereco.setUf(dto.uf());
        endereco.setComplemento(dto.complemento());
        imovel.setEndereco(endereco);
    }

    private ImovelResponseDto toResponseDto(Imovel imovel) {
        final var disponivel = !contratoService.existsContratoByImovelId(imovel.getId());
        final var endereco = imovel.getEndereco() != null ? imovel.getEndereco().toString() : "";
        return new ImovelResponseDto(
                imovel.getId(),
                imovel.getDescricao(),
                endereco,
                imovel.getValorAluguelSugerido(),
                disponivel
        );
    }
}
