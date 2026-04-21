package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.response.ImovelResponseDto;
import com.mscosta.imoblygestapi.service.ImovelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/imovel")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> getImovelById(@PathVariable("id") long id) {
        final var response = imovelService.getImovelById(id);
        return response != null
                ? ResponseEntity.ok(response)
                : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<ImovelResponseDto>> getAllImoveis(ImovelRequestDto request) {
        final var pageable = PageRequest.of(request.page(), request.size());
        final var response = imovelService.getAllImoveis(request, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ImovelResponseDto> criarImovel(@RequestBody ImovelRequestDto request) {
        final var response = imovelService.criarImovel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> atualizarImovel(
            @PathVariable("id") long id,
            @RequestBody ImovelRequestDto request) {
        final var response = imovelService.atualizarImovel(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable("id") long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}
