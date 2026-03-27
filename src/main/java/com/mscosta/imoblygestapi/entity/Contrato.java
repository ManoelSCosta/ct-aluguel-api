package com.mscosta.imoblygestapi.entity;

import com.mscosta.imoblygestapi.config.database.DatabaseConstants;
import com.mscosta.imoblygestapi.enums.StatusContrato;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = DatabaseConstants.Tables.CONTRATO, schema = DatabaseConstants.SCHEMA_NAME)
public class Contrato {

    @Id
    @SequenceGenerator(
            name = DatabaseConstants.Sequences.SEQ_CONTRATO_ID,
            sequenceName = DatabaseConstants.Sequences.SEQ_CONTRATO_ID,
            allocationSize = 1
    )
    @GeneratedValue(generator = DatabaseConstants.Sequences.SEQ_CONTRATO_ID, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @Column(name = "data_inicio_contrato")
    private LocalDate dataInicioContrato;

    @Column(name = "data_fim_contrato")
    private LocalDate dataFimContrato;

    @Column(name = "valor_aluguel")
    private BigDecimal valorAluguel;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusContrato statusContrato;

    @ManyToOne
    @JoinColumn(name = "id_imovel")
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "id_inquilino")
    private Pessoa inquilino;

    @ManyToOne
    @JoinColumn(name = "id_locador")
    private Pessoa locador;

    public Contrato() {
    }

    public Contrato(long id, LocalDateTime dataCriacao, StatusContrato statusContrato, LocalDate dataInicioContrato,
                    BigDecimal valorAluguel, Imovel imovel, Pessoa inquilino, Pessoa locatario) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataInicioContrato = dataInicioContrato;
        this.valorAluguel = valorAluguel;
        this.statusContrato = statusContrato;
        this.imovel = imovel;
        this.inquilino = inquilino;
        this.locador = locatario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(LocalDateTime dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public LocalDate getDataInicioContrato() {
        return dataInicioContrato;
    }

    public void setDataInicioContrato(LocalDate dataInicioContrato) {
        this.dataInicioContrato = dataInicioContrato;
    }

    public LocalDate getDataFimContrato() {
        return dataFimContrato;
    }

    public void setDataFimContrato(LocalDate dataFimContrato) {
        this.dataFimContrato = dataFimContrato;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Pessoa getInquilino() {
        return inquilino;
    }

    public void setInquilino(Pessoa inquilino) {
        this.inquilino = inquilino;
    }

    public Pessoa getLocador() {
        return locador;
    }

    public void setLocador(Pessoa locador) {
        this.locador = locador;
    }

    public StatusContrato getStatusContrato() {
        return statusContrato;
    }

    public void setStatusContrato(StatusContrato statusContrato) {
        this.statusContrato = statusContrato;
    }
}