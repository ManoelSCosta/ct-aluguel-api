package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.dto.response.PessoaResponseDto;
import com.mscosta.imoblygestapi.entity.Pessoa;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private static final Logger log = LoggerFactory.getLogger(PessoaService.class);
    private static final String PESSOA_NAO_ENCONTRADA_MESSAGE = "Pessoa não encontrada";

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public PessoaResponseDto createPessoa(PessoaRequestDto request) {
        log.info("Criando nova pessoa com nome: {}", request.nome());
        final var pessoa = toEntity(request);
        final var savedPessoa = savePessoa(pessoa);
        log.info("Pessoa criada com sucesso. ID: {}", savedPessoa.getId());
        return toResponseDto(savedPessoa);
    }

    public PessoaResponseDto getPessoaById(long id) {
        log.info("Buscando pessoa por ID: {}", id);
        final var pessoa = findPessoaById(id);
        log.debug("Pessoa encontrada: {}", pessoa.getNome());
        return toResponseDto(pessoa);
    }

    public PessoaResponseDto updatePessoa(long id, PessoaRequestDto request) {
        log.info("Atualizando pessoa com ID: {}", id);
        final var pessoa = findPessoaById(id);
        updateEntity(pessoa, request);
        final var updatedPessoa = savePessoa(pessoa);
        log.info("Pessoa atualizada com sucesso. ID: {}", id);
        return toResponseDto(updatedPessoa);
    }

    public void deletePessoa(long id) {
        log.info("Deletando pessoa com ID: {}", id);
        pessoaRepository.deleteById(id);
        log.info("Pessoa deletada com sucesso. ID: {}", id);
    }

    private Pessoa savePessoa(Pessoa pessoa) {
        log.debug("Salvando pessoa no banco de dados");
        return pessoaRepository.save(pessoa);
    }

    private Pessoa findPessoaById(long id) {
        log.debug("Buscando pessoa no repositório. ID: {}", id);
        return pessoaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pessoa não encontrada. ID: {}", id);
                    return new NotFoundException(PESSOA_NAO_ENCONTRADA_MESSAGE);
                });
    }

    private Pessoa toEntity(PessoaRequestDto request) {
        final var pessoa = new Pessoa();
        pessoa.setNome(request.nome());
        pessoa.setContato(request.contato());
        return pessoa;
    }

    private void updateEntity(Pessoa pessoa, PessoaRequestDto request) {
        log.debug("Atualizando entidade pessoa. ID: {}", pessoa.getId());
        pessoa.setNome(request.nome());
        pessoa.setContato(request.contato());
    }

    private PessoaResponseDto toResponseDto(Pessoa pessoa) {
        final var response = new PessoaResponseDto();
        response.setNome(pessoa.getNome());
        response.setContato(pessoa.getContato());
        return response;
    }
}
