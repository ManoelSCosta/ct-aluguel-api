package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.dto.response.PessoaResponseDto;
import com.mscosta.imoblygestapi.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pessoa")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDto> cadastrarPessoa(@RequestBody PessoaRequestDto pessoaRequestDto) {
        final var pessoaResponseDto = pessoaService.createPessoa(pessoaRequestDto);
        return ResponseEntity.ok(pessoaResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> getPessoaById(@PathVariable long id) {
        final var pessoaResponseDto = pessoaService.getPessoaById(id);
        return ResponseEntity.ok(pessoaResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> atualizarPessoa(@PathVariable long id, @RequestBody PessoaRequestDto pessoaRequestDto) {
        final var pessoaResponseDto = pessoaService.updatePessoa(id, pessoaRequestDto);
        return ResponseEntity.ok(pessoaResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable long id) {
        pessoaService.deletePessoa(id);
        return ResponseEntity.noContent().build();
    }
}
