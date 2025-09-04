package com.programacao.web.fatec.api_fatec.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma cidade no sistema.
 * Uma cidade possui um id, um nome e um estado.
 * Uma cidade pode estar relacionada a vários clientes (relacionamento 1:N).
 */
@Entity
@Table(name = "cidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado;

    /**
     * Lista de clientes associados a esta cidade.
     * Uma cidade pode ter vários clientes (relacionamento 1:N).
     * mappedBy = "cidade" indica que o campo "cidade" na entidade Cliente
     * é o proprietário do relacionamento.
     * 
     * @JsonManagedReference é usado para evitar recursão infinita na serialização JSON.
     * Este lado do relacionamento será serializado normalmente.
     */
    @OneToMany(mappedBy = "cidade")
    @JsonManagedReference
    private List<Cliente> clientes = new ArrayList<>();

    /**
     * Construtor para criar uma cidade sem clientes inicialmente.
     * 
     * @param id O ID da cidade (pode ser null para novas cidades)
     * @param nome O nome da cidade
     * @param estado O estado ao qual a cidade pertence
     */
    public Cidade(Long id, String nome, Estado estado) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
    }
}
