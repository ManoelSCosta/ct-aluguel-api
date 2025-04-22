package com.mscosta.imoblygestapi.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "cta_contrato")
public class Contrato {

    @Id
    @SequenceGenerator(name = "seq_cta_contrato_id", sequenceName = "seq_cta_contrato_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_cta_contrato_id", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "data_criacao")
    private Date dataCriacao;
    @Column(name = "data_modificacao")
    private Date dataModificacao;
    @Column(name = "data_inicio_contrato")
    private Date dataInicioContrato;
    @Column(name = "data_fim_contrato")
    private Date dataFimContrato;
    @Column(name = "valor_aluguel")
    private Double valorAluguel;
    @ManyToOne
    @JoinColumn(name = "id_casa")
    private Casa casa;
    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Pessoa inquilino;
    @ManyToOne
    @JoinColumn(name = "id_locatario")
    private Pessoa locatario;

    public Contrato() {}

    public Contrato(long id, Date dataCriacao, Date dataModificacao, Date dataInicioContrato, Date dataFimContrato, Double valorAluguel, Casa casa, Pessoa inquilino, Pessoa locatario) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.dataInicioContrato = dataInicioContrato;
        this.dataFimContrato = dataFimContrato;
        this.valorAluguel = valorAluguel;
        this.casa = casa;
        this.inquilino = inquilino;
        this.locatario = locatario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

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

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public Pessoa getInquilino() {
        return inquilino;
    }

    public void setInquilino(Pessoa inquilino) {
        this.inquilino = inquilino;
    }

    public Pessoa getLocatario() {
        return locatario;
    }

    public void setLocatario(Pessoa locatario) {
        this.locatario = locatario;
    }
}