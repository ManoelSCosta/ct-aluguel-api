package com.mscosta.imoblygestapi.controller;

import com.mscosta.imoblygestapi.dto.request.PagamentoRequestDto;
import com.mscosta.imoblygestapi.dto.response.PagamentoResponseDto;
import com.mscosta.imoblygestapi.service.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/pagamento")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PagamentoResponseDto> registrarPagamento(
            @RequestPart("pagamento") PagamentoRequestDto request,
            @RequestPart(value = "comprovante", required = false) MultipartFile comprovante) throws IOException {
        
        byte[] comprovanteBytes = (comprovante != null) ? comprovante.getBytes() : null;
        final var response = pagamentoService.registrarPagamento(request, comprovanteBytes);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
