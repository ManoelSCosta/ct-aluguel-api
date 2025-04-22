package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.PessoaRequestDto;
import com.mscosta.imoblygestapi.dto.response.PessoaResponseDto;
import com.mscosta.imoblygestapi.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pessoa")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;
    @PostMapping
    public ResponseEntity<PessoaResponseDto> cadastarPessoa(PessoaRequestDto pessoaRequestDto) {
        try {
           final var pessoaResponseDto = pessoaService.createPessoa(pessoaRequestDto);
            return ResponseEntity.ok(pessoaResponseDto);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDto> getPessoaById(@PathVariable("id") long id) {
        try {
            final var response = pessoaService.getPessoaById(id);
            return new ResponseEntity<>(response, response != null ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }
}
