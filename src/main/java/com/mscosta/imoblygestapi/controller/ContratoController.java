package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.response.ContratoResponseDto;
import com.mscosta.imoblygestapi.service.ContratoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contrato")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDto> abrirContrato(@Valid @RequestBody ContratoRequestDto request) {
        final var response = contratoService.abrirContrato(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ContratoResponseDto>> listarContratosAtivos() {
        return ResponseEntity.ok(contratoService.listarContratosAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDto> getContratoById(@PathVariable("id") long id) {
        return ResponseEntity.ok(contratoService.getContratoById(id));
    }
}
