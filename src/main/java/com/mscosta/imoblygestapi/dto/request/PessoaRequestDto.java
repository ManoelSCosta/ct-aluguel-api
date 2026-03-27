package com.mscosta.imoblygestapi.dto.request;

public class PessoaRequestDto {
    private String nome;
    private String contato;
    private String cpf;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
