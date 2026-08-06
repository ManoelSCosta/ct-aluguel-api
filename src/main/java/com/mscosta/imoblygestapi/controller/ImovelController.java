package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.ImovelFiltroDto;
import com.mscosta.imoblygestapi.dto.request.ImovelRequestDto;
import com.mscosta.imoblygestapi.dto.response.ImovelResponseDto;
import com.mscosta.imoblygestapi.service.ImovelService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@RequestMapping("/api/v1/imovel")
public class ImovelController {

    private final ImovelService imovelService;

    public ImovelController(ImovelService imovelService) {
        this.imovelService = imovelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> getImovelById(@PathVariable("id") long id) {
        return ResponseEntity.ok(imovelService.getImovelById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ImovelResponseDto>> getAllImoveis(ImovelFiltroDto filtro) {
        final var pageable = PageRequest.of(filtro.paginaOuPadrao(), filtro.tamanhoOuPadrao());
        return ResponseEntity.ok(imovelService.getAllImoveis(filtro, pageable));
    }

    @PostMapping
    public ResponseEntity<ImovelResponseDto> criarImovel(@Valid @RequestBody ImovelRequestDto request) {
        final var response = imovelService.criarImovel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ImovelResponseDto> atualizarImovel(
            @PathVariable("id") long id,
            @Valid @RequestBody ImovelRequestDto request) {
        return ResponseEntity.ok(imovelService.atualizarImovel(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarImovel(@PathVariable("id") long id) {
        imovelService.deletarImovel(id);
        return ResponseEntity.noContent().build();
    }
}
