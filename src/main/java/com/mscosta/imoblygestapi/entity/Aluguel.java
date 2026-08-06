package com.mscosta.imoblygestapi.entity;

import com.mscosta.imoblygestapi.config.database.DatabaseConstants;
import com.mscosta.imoblygestapi.enums.StatusAluguel;
import com.mscosta.imoblygestapi.enums.converter.StatusAluguelConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = DatabaseConstants.Tables.ALUGUEL, schema = DatabaseConstants.SCHEMA_NAME)
public class Aluguel {

    private static final BigDecimal VALOR_PAGO_PADRAO = BigDecimal.ZERO;

    @Id
    @SequenceGenerator(name = DatabaseConstants.Sequences.SEQ_ALUGUEL_ID, sequenceName = DatabaseConstants.Sequences.SEQ_ALUGUEL_ID, allocationSize = 1)
    @GeneratedValue(generator = DatabaseConstants.Sequences.SEQ_ALUGUEL_ID, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Convert(converter = StatusAluguelConverter.class)
    @Column(name = "status", nullable = false, length = 1)
    private StatusAluguel status;

    @Column(name = "ano_referencia", nullable = false)
    private Integer anoReferencia;

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "valor_previsto", nullable = false)
    private BigDecimal valorPrevisto;

    @Column(name = "valor_pago", nullable = false)
    private BigDecimal valorPago = VALOR_PAGO_PADRAO;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @ManyToOne
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @OneToMany(mappedBy = "aluguel", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Pagamento> pagamentos = new ArrayList<>();

    public Aluguel() {
    }

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataModificacao = LocalDateTime.now();
    }

    public Aluguel(Long id, StatusAluguel status, Integer anoReferencia, Integer mesReferencia, LocalDate dataVencimento, BigDecimal valorPrevisto, Contrato contrato, LocalDateTime dataCriacao, LocalDateTime dataModificacao) {
        this.id = id;
        this.status = status;
        this.anoReferencia = anoReferencia;
        this.mesReferencia = mesReferencia;
        this.dataVencimento = dataVencimento;
        this.valorPrevisto = valorPrevisto;
        this.dataCriacao = dataCriacao;
        this.dataModificacao = dataModificacao;
        this.contrato = contrato;
    }

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

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public List<Pagamento> getPagamentos() {
        return List.copyOf(pagamentos);
    }

    public void adicionarPagamento(Pagamento pagamento) {
        Pagamento pagamentoValido = Objects.requireNonNull(pagamento, "pagamento não pode ser nulo");
        pagamentos.add(pagamentoValido);
        vincularPagamentoAoAluguel(pagamentoValido);
    }

    public void removerPagamento(Pagamento pagamento) {
        if (pagamentos.remove(pagamento) && pagamento != null) {
            pagamento.setAluguel(null);
        }
    }

    private void vincularPagamentoAoAluguel(Pagamento pagamento) {
        pagamento.setAluguel(this);
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
}
