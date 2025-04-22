package com.mscosta.imoblygestapi.dto.request;

import java.util.Date;

public class ContratoRequestDto {
    private Date dataInicioContrato;
    private Date dataFimContrato;
    private Double valorAluguel;
    private CasaRequestDto casa;
    private PessoaRequestDto inquilino;
    private PessoaRequestDto locatario;

    public Date getDataInicioContrato() {
        return dataInicioContrato;
    }

    public void setDataInicioContrato(Date dataInicioContrato) {
        this.dataInicioContrato = dataInicioContrato;
    }

    public Date getDataFimContrato() {
        return dataFimContrato;
    }

    public void setDataFimContrato(Date dataFimContrato) {
        this.dataFimContrato = dataFimContrato;
    }

    public Double getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(Double valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public CasaRequestDto getCasa() {
        return casa;
    }

    public void setCasa(CasaRequestDto casa) {
        this.casa = casa;
    }

    public PessoaRequestDto getInquilino() {
        return inquilino;
    }

    public void setInquilino(PessoaRequestDto inquilino) {
        this.inquilino = inquilino;
    }

    public PessoaRequestDto getLocatario() {
        return locatario;
    }

    public void setLocatario(PessoaRequestDto locatario) {
        this.locatario = locatario;
    }
}
