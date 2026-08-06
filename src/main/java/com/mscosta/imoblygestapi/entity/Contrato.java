package com.mscosta.imoblygestapi.entity;

import com.mscosta.imoblygestapi.config.database.DatabaseConstants;
import com.mscosta.imoblygestapi.enums.StatusContrato;
import com.mscosta.imoblygestapi.enums.TipoGarantia;
import com.mscosta.imoblygestapi.enums.converter.StatusContratoConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = DatabaseConstants.Tables.CONTRATO, schema = DatabaseConstants.SCHEMA_NAME)
public class Contrato {

    private static final Integer DIA_VENCIMENTO_PADRAO = 5;
    private static final BigDecimal MULTA_ATRASO_PADRAO = new BigDecimal("10.00");
    private static final BigDecimal JUROS_MES_PADRAO = new BigDecimal("1.00");

    @Id
    @SequenceGenerator(
            name = DatabaseConstants.Sequences.SEQ_CONTRATO_ID,
            sequenceName = DatabaseConstants.Sequences.SEQ_CONTRATO_ID,
            allocationSize = 1
    )
    @GeneratedValue(generator = DatabaseConstants.Sequences.SEQ_CONTRATO_ID, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_inicio_contrato", nullable = false)
    private LocalDate dataInicioContrato;

    @Column(name = "data_fim_contrato", nullable = false)
    private LocalDate dataFimContrato;

    @Column(name = "valor_aluguel", nullable = false)
    private BigDecimal valorAluguel;

    /** Dia do mês em que cada aluguel vence. O banco restringe o intervalo a 1..28. */
    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento = DIA_VENCIMENTO_PADRAO;

    @Column(name = "multa_atraso_perc", nullable = false)
    private BigDecimal multaAtrasoPerc = MULTA_ATRASO_PADRAO;

    @Column(name = "juros_mes_perc", nullable = false)
    private BigDecimal jurosMesPerc = JUROS_MES_PADRAO;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_garantia", nullable = false, length = 50)
    private TipoGarantia tipoGarantia;

    @Convert(converter = StatusContratoConverter.class)
    @Column(name = "status", nullable = false, length = 1)
    private StatusContrato statusContrato;

    @ManyToOne
    @JoinColumn(name = "id_imovel", nullable = false)
    private Imovel imovel;

    @ManyToOne
    @JoinColumn(name = "id_inquilino", nullable = false)
    private Pessoa inquilino;

    @ManyToOne
    @JoinColumn(name = "id_locador", nullable = false)
    private Pessoa locador;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    public Contrato() {
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataModificacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public BigDecimal getMultaAtrasoPerc() {
        return multaAtrasoPerc;
    }

    public void setMultaAtrasoPerc(BigDecimal multaAtrasoPerc) {
        this.multaAtrasoPerc = multaAtrasoPerc;
    }

    public BigDecimal getJurosMesPerc() {
        return jurosMesPerc;
    }

    public void setJurosMesPerc(BigDecimal jurosMesPerc) {
        this.jurosMesPerc = jurosMesPerc;
    }

    public TipoGarantia getTipoGarantia() {
        return tipoGarantia;
    }

    public void setTipoGarantia(TipoGarantia tipoGarantia) {
        this.tipoGarantia = tipoGarantia;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contrato contrato)) {
            return false;
        }
        return id != null && Objects.equals(id, contrato.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
