package com.mscosta.imoblygestapi.entity;

import com.mscosta.imoblygestapi.config.database.DatabaseConstants;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = DatabaseConstants.Tables.IMOVEL, schema = DatabaseConstants.SCHEMA_NAME)
public class Imovel {

    @Id
    @SequenceGenerator(
            name = DatabaseConstants.Sequences.SEQ_IMOVEL_ID,
            sequenceName = DatabaseConstants.Sequences.SEQ_IMOVEL_ID,
            allocationSize = 1
    )
    @GeneratedValue(generator = DatabaseConstants.Sequences.SEQ_IMOVEL_ID, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "matricula_rgi") // Vital para o contrato
    private String matriculaRgi;

    @Column(name = "inscricao_iptu") // Identificação na prefeitura
    private String inscricaoIptu;

    /** Valor de referência para novos contratos; o valor efetivo fica no contrato. */
    @Column(name = "valor_aluguel_sugerido", nullable = false)
    private BigDecimal valorAluguelSugerido;

    @OneToOne(cascade = CascadeType.ALL) // Se apagar o imóvel, geralmente apaga o endereço
    @JoinColumn(name = "id_endereco", referencedColumnName = "id", nullable = false)
    private Endereco endereco;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataModificacao = LocalDateTime.now();
    }

    public Imovel() {
    }

    public Imovel(String descricao, Endereco endereco) {
        this.descricao = descricao;
        this.endereco = endereco;
    }

    public Imovel(Long id, String descricao, Endereco endereco) {
        this.id = id;
        this.descricao = descricao;
        this.endereco = endereco;
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

    public String getMatriculaRgi() {
        return matriculaRgi;
    }

    public void setMatriculaRgi(String matriculaRgi) {
        this.matriculaRgi = matriculaRgi;
    }

    public String getInscricaoIptu() {
        return inscricaoIptu;
    }

    public void setInscricaoIptu(String inscricaoIptu) {
        this.inscricaoIptu = inscricaoIptu;
    }

    public BigDecimal getValorAluguelSugerido() {
        return valorAluguelSugerido;
    }

    public void setValorAluguelSugerido(BigDecimal valorAluguelSugerido) {
        this.valorAluguelSugerido = valorAluguelSugerido;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
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
