package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.dto.response.PessoaResponseDto;
import com.mscosta.imoblygestapi.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pessoa")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDto> cadastrarPessoa(@Valid @RequestBody PessoaRequestDto request) {
        final var response = pessoaService.createPessoa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> getPessoaById(@PathVariable long id) {
        return ResponseEntity.ok(pessoaService.getPessoaById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> atualizarPessoa(@PathVariable long id,
                                                             @Valid @RequestBody PessoaRequestDto request) {
        return ResponseEntity.ok(pessoaService.updatePessoa(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable long id) {
        pessoaService.deletePessoa(id);
        return ResponseEntity.noContent().build();
    }
}
