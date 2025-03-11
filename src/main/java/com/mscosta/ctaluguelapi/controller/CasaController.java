package com.mscosta.ctaluguelapi.controller;

import com.mscosta.ctaluguelapi.dto.request.CasaRequestDto;
import com.mscosta.ctaluguelapi.dto.response.CasaResponseDto;
import com.mscosta.ctaluguelapi.service.CasaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/casa")
public class CasaController {
    @Autowired
    private CasaService casaService;



    @GetMapping("/{id}")
    public ResponseEntity<CasaResponseDto> getCasaById(@PathVariable("id") long id) {
        final var response = casaService.getCasaById(id);
        return new ResponseEntity<>(response, response != null ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @GetMapping()
    public ResponseEntity<Page<CasaResponseDto>> getAllCasas(CasaRequestDto request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
        final var response = casaService.getAllCasas(request, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<CasaResponseDto> inserirCasa(CasaRequestDto request) {
        final var response = casaService.criarCasa(request);
        return new ResponseEntity<>(response, response != null ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CasaResponseDto> atualizarCasa(@PathVariable("id") long id, @RequestBody CasaRequestDto request) {
        final var response = casaService.atualizarCasa(id, request);
        return new ResponseEntity<>(response, response != null ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCasa(@PathVariable("id") long id) {
        casaService.deletarCasa(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
