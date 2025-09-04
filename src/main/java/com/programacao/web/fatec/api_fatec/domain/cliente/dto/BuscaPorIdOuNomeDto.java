package com.programacao.web.fatec.api_fatec.domain.cliente.dto;

public class BuscaPorIdOuNomeDto {
    public Long id;

    public String nome;

    public BuscaPorIdOuNomeDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public BuscaPorIdOuNomeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
