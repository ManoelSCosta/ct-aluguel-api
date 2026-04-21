package com.mscosta.imoblygestapi.dto.response;

import com.mscosta.imoblygestapi.enums.StatusAluguel;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AluguelResponseDto {
    private Long id;
    private StatusAluguel status;
    private Integer anoReferencia;
    private Integer mesReferencia;
    private LocalDate dataVencimento;
    private BigDecimal valorPrevisto;
    private BigDecimal valorPago;
    private Long idContrato;
    private List<PagamentoResponseDto> pagamentos;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusAluguel getStatus() {
        return status;
    }

    public void setStatus(StatusAluguel status) {
        this.status = status;
    }

    public Integer getAnoReferencia() {
        return anoReferencia;
    }

    public void setAnoReferencia(Integer anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValorPrevisto() {
        return valorPrevisto;
    }

    public void setValorPrevisto(BigDecimal valorPrevisto) {
        this.valorPrevisto = valorPrevisto;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public Long getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Long idContrato) {
        this.idContrato = idContrato;
    }

    public List<PagamentoResponseDto> getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(List<PagamentoResponseDto> pagamentos) {
        this.pagamentos = pagamentos;
    }
}
