package com.mscosta.imoblygestapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cta_aluguel")
public class Aluguel {

    @Id
    @SequenceGenerator(name = "seq_cta_aluguel_id", sequenceName = "seq_cta_aluguel_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_cta_aluguel_id", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia;


    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    public Aluguel() {}

    public Aluguel(long id, String status, Integer mesReferencia, Contrato contrato) {
        this.id = id;
        this.status = status;
        this.mesReferencia = mesReferencia;
        this.contrato = contrato;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Integer mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}
