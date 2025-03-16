package com.mscosta.ctaluguelapi.service;

import com.mscosta.ctaluguelapi.dto.request.PessoaRequestDto;
import com.mscosta.ctaluguelapi.dto.response.PessoaResponseDto;
import com.mscosta.ctaluguelapi.exception.NotFoundException;
import com.mscosta.ctaluguelapi.model.Pessoa;
import com.mscosta.ctaluguelapi.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public PessoaResponseDto createPessoa(PessoaRequestDto request) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(request.getNome());
        pessoa.setContato(request.getContato());
        savePessoa(pessoa);
        return buildResponse(pessoa);
    }

    public PessoaResponseDto getPessoaById(long id) {
        var pessoa = findPessoaById(id);
        return buildResponse(pessoa);
    }

    public PessoaResponseDto updatePessoa(long id,PessoaRequestDto request) {
        final var pessoa = findPessoaById(id);
        pessoa.setNome(request.getNome());
        pessoa.setContato(request.getContato());
        savePessoa(pessoa);
        return buildResponse(pessoa);
    }

    public void deletePessoa(long id) {
        pessoaRepository.deleteById(id);
    }
    private void savePessoa(Pessoa pessoa) {
        pessoaRepository.save(pessoa);
    }
    private Pessoa findPessoaById(long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pessoa não encontrada"));
    }
    private PessoaResponseDto buildResponse(Pessoa pessoaSave) {
        final var response = new PessoaResponseDto();
        response.setNome(pessoaSave.getNome());
        response.setContato(pessoaSave.getContato());
        return response;
    }

}
