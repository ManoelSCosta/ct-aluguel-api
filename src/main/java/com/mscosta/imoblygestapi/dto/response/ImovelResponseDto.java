package com.mscosta.imoblygestapi.dto.response;

public class ImovelResponseDto {
    private String descricao;
    private String endereco;
    private Boolean disponivel;

    public ImovelResponseDto(String descricao, String endereco, Boolean disponivel) {
        this.descricao = descricao;
        this.endereco = endereco;
        this.disponivel = disponivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
}
