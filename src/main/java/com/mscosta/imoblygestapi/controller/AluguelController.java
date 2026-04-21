package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.response.AluguelResponseDto;
import com.mscosta.imoblygestapi.service.AluguelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aluguel")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @GetMapping("/contrato/{contratoId}")
    public ResponseEntity<List<AluguelResponseDto>> listarAlugueisPorContrato(@PathVariable("contratoId") Long contratoId) {
        final var response = aluguelService.listarAlugueisPorContrato(contratoId);
        return ResponseEntity.ok(response);
    }
}
