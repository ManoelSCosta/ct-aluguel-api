package com.mscosta.imoblygestapi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cta_casa", schema = "public")
public class Casa {
    @Id
    @SequenceGenerator(name = "seq_cta_casa_id", sequenceName = "seq_cta_casa_id", allocationSize = 1)
    @GeneratedValue(generator = "seq_cta_casa_id", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "descricao", nullable = false)
    private String descricao;
    @Column(name = "endereco", nullable = false)
    private String endereco;
    public Casa() {}
    public Casa(long id, String descricao, String endereco) {
        this.id = id;
        this.descricao = descricao;
        this.endereco = endereco;
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
    public String getEndereco() {
        return this.endereco;
    }
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
