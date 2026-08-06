package com.mscosta.imoblygestapi.entity;

import com.mscosta.imoblygestapi.config.database.DatabaseConstants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = DatabaseConstants.Tables.PAGAMENTO, schema = DatabaseConstants.SCHEMA_NAME)
public class Pagamento {

    @Id
    @SequenceGenerator(
            name = DatabaseConstants.Sequences.SEQ_PAGAMENTO_ID,
            sequenceName = DatabaseConstants.Sequences.SEQ_PAGAMENTO_ID,
            allocationSize = 1
    )
    @GeneratedValue(generator = DatabaseConstants.Sequences.SEQ_PAGAMENTO_ID, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "valor_pagamento", nullable = false)
    private BigDecimal valorPagamento;

    @Column(name = "data_pagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column(name = "comprovante_bytea", columnDefinition = "bytea")
    private byte[] comprovantePagamento;

    @ManyToOne
    @JoinColumn(name = "id_aluguel", nullable = false)
    private Aluguel aluguel;

    protected Pagamento() {
    }

    public Pagamento(Long id,
                     String descricao,
                     BigDecimal valorPagamento,
                     LocalDateTime dataPagamento,
                     byte[] comprovantePagamento,
                     Aluguel aluguel) {
        this.id = id;
        this.descricao = Objects.requireNonNull(descricao, "descricao não pode ser nula");
        this.valorPagamento = Objects.requireNonNull(valorPagamento, "valorPagamento não pode ser nulo");
        this.dataPagamento = Objects.requireNonNull(dataPagamento, "dataPagamento não pode ser nulo");
        this.comprovantePagamento = comprovantePagamento;
        this.aluguel = aluguel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public byte[] getComprovantePagamento() {
        return comprovantePagamento;
    }

    public void setComprovantePagamento(byte[] comprovantePagamento) {
        this.comprovantePagamento = comprovantePagamento;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pagamento pagamento)) {
            return false;
        }
        return id != null && Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
