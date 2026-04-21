package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.ContratoRequestDto;
import com.mscosta.imoblygestapi.dto.response.ContratoResponseDto;
import com.mscosta.imoblygestapi.service.ContratoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contrato")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<ContratoResponseDto> abrirContrato(@RequestBody ContratoRequestDto request) {
        final var response = contratoService.abrirContrato(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ContratoResponseDto>> listarContratosAtivos() {
        final var response = contratoService.listarContratosAtivos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDto> getContratoById(@PathVariable("id") long id) {
        final var response = contratoService.getContratoById(id);
        return ResponseEntity.ok(response);
    }
}
