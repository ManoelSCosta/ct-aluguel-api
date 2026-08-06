package com.mscosta.imoblygestapi.service;

import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.dto.response.PessoaResponseDto;
import com.mscosta.imoblygestapi.entity.Pessoa;
import com.mscosta.imoblygestapi.exception.NotFoundException;
import com.mscosta.imoblygestapi.repository.PessoaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PessoaService {

    private static final Logger log = LoggerFactory.getLogger(PessoaService.class);
    private static final String PESSOA_NAO_ENCONTRADA_MESSAGE = "Pessoa não encontrada";

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public PessoaResponseDto createPessoa(PessoaRequestDto request) {
        log.info("Criando nova pessoa com nome: {}", request.nome());
        final var pessoa = new Pessoa();
        aplicarDados(pessoa, request);
        final var savedPessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada com sucesso. ID: {}", savedPessoa.getId());
        return toResponseDto(savedPessoa);
    }

    @Transactional(readOnly = true)
    public PessoaResponseDto getPessoaById(long id) {
        log.info("Buscando pessoa por ID: {}", id);
        return toResponseDto(findPessoaById(id));
    }

    @Transactional
    public PessoaResponseDto updatePessoa(long id, PessoaRequestDto request) {
        log.info("Atualizando pessoa com ID: {}", id);
        final var pessoa = findPessoaById(id);
        aplicarDados(pessoa, request);
        final var updatedPessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa atualizada com sucesso. ID: {}", id);
        return toResponseDto(updatedPessoa);
    }

    @Transactional
    public void deletePessoa(long id) {
        log.info("Deletando pessoa com ID: {}", id);
        pessoaRepository.deleteById(id);
        log.info("Pessoa deletada com sucesso. ID: {}", id);
    }

    private Pessoa findPessoaById(long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pessoa não encontrada. ID: {}", id);
                    return new NotFoundException(PESSOA_NAO_ENCONTRADA_MESSAGE);
                });
    }

    private void aplicarDados(Pessoa pessoa, PessoaRequestDto request) {
        pessoa.setNome(request.nome());
        pessoa.setCpf(request.cpf());
        pessoa.setEmail(request.email());
        pessoa.setTelefone(request.telefone());
        pessoa.setRg(request.rg());
        pessoa.setEstadoCivil(request.estadoCivil());
        pessoa.setProfissao(request.profissao());
        if (request.nacionalidade() != null) {
            pessoa.setNacionalidade(request.nacionalidade());
        }
    }

    private PessoaResponseDto toResponseDto(Pessoa pessoa) {
        return new PessoaResponseDto(
                pessoa.getId(),
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getTelefone()
        );
    }
}
