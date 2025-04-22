package com.mscosta.imoblygestapi.dto.request;

public class PessoaRequestDto {
    private String nome;
    private String contato;


    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getContato() {
        return contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
