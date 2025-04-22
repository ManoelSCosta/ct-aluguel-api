package com.mscosta.imoblygestapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cta_pagamento")
public class Pagamento {

    @Id
    @SequenceGenerator(name = "seq_cta_pagamento_id", sequenceName = "seq_cta_pagamento_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_cta_pagamento_id", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "valor_pagamento", nullable = false)
    private Double valorPago;
    @Column(name = "status", nullable = false)
    private String status;
    @ManyToOne
    @JoinColumn(name = "aluguel_id")
    private Aluguel aluguel;

    public Pagamento() {}

    public Pagamento(long id, String descricao, Double valorPago, String status, Aluguel aluguel) {
        this.id = id;
        this.descricao = descricao;
        this.valorPago = valorPago;
        this.status = status;
        this.aluguel = aluguel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValorPago() {
        return valorPago;
    }

    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }
}
