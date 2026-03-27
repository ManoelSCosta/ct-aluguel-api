package com.mscosta.imoblygestapi.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagamentoRequestDto {
    private String descricao;
    private BigDecimal valorPagamento;
    private LocalDateTime dataPagamento;
    private Long idAluguel;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    public void setValorPagamento(BigDecimal valorPagamento) {
        this.valorPagamento = valorPagamento;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Long getIdAluguel() {
        return idAluguel;
    }

    public void setIdAluguel(Long idAluguel) {
        this.idAluguel = idAluguel;
    }
}
