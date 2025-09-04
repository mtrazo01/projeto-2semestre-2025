package com.programacao.web.fatec.api_fatec.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um cliente no sistema.
 * Um cliente possui um id, um nome, um endereço e está associado a uma cidade.
 */
@Entity
@Table(name="clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nome;

    @Column(nullable = false, length = 60)
    private String endereco;

    /**
     * Relacionamento ManyToOne com a entidade Cidade.
     * Muitos clientes podem estar associados a uma única cidade (relacionamento N:1).
     * A coluna cidade_id na tabela clientes será a chave estrangeira.
     * 
     * FORMATO ATUAL (retorna cidade e campos cidade_*):
     * {
     *   "id": 5,
     *   "nome": "Pedro",
     *   "endereco": "Av. Beira Mar, 789",
     *   "cidade": {
     *     "id": 5,
     *     "nome": "Fortaleza",
     *     "estado": "CE"
     *   },
     *   "cidade_nome": "Fortaleza",
     *   "cidade_estado": "CE",
     *   "cidade_id": 5
     * }
     * 
     * FORMATO 1 (sem o objeto cidade, apenas campos cidade_*):
     * Para obter este formato, adicione a anotação @JsonIgnore ao campo cidade:
     * @JsonIgnore
     * 
     * {
     *   "id": 5,
     *   "nome": "Pedro",
     *   "endereco": "Av. Beira Mar, 789",
     *   "cidade_nome": "Fortaleza",
     *   "cidade_estado": "CE",
     *   "cidade_id": 5
     * }
     * 
     * FORMATO 2 (apenas o objeto cidade, sem campos cidade_*):
     * Para obter este formato, remova a anotação @JsonIgnore do campo cidade (se presente)
     * e adicione @JsonIgnore aos métodos getCidadeNome(), getCidadeEstado() e getCidadeId()
     * 
     * {
     *   "id": 5,
     *   "nome": "Pedro",
     *   "endereco": "Av. Beira Mar, 789",
     *   "cidade": {
     *     "id": 5,
     *     "nome": "Fortaleza",
     *     "estado": "CE"
     *   }
     * }
     * 
     * @JsonIgnoreProperties("clientes") é usado para evitar recursão infinita na serialização JSON.
     * Permite que o objeto cidade seja incluído na resposta JSON, mas ignora o campo clientes da cidade.
     */
    @ManyToOne
    @JoinColumn(name = "cidade_id")
    @JsonIgnoreProperties("clientes")
    // Descomente a linha abaixo para obter o FORMATO 1 (sem o objeto cidade)
    //@JsonIgnore
    private Cidade cidade;

    /**
     * Construtor para criar um cliente sem associá-lo a uma cidade.
     * 
     * @param id O ID do cliente (pode ser null para novos clientes)
     * @param nome O nome do cliente
     * @param endereco O endereço do cliente
     */
    public Cliente(Long id, String nome, String endereco) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
    }

    /**
     * Construtor para criar um cliente associado a uma cidade.
     * 
     * @param id O ID do cliente (pode ser null para novos clientes)
     * @param nome O nome do cliente
     * @param endereco O endereço do cliente
     * @param cidade A cidade à qual o cliente está associado
     */
    public Cliente(Long id, String nome, String endereco, Cidade cidade) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
    }

    /**
     * Retorna o nome da cidade do cliente.
     * Este método é usado apenas para serialização JSON e não é persistido no banco de dados.
     *
     * Para implementar o FORMATO 2 (apenas o objeto cidade, sem campos cidade_*),
     * adicione a anotação @JsonIgnore a este método.
     *
     * @return O nome da cidade do cliente, ou null se o cliente não estiver associado a uma cidade
     */
    @JsonProperty("cidade_nome")
    // Descomente a linha abaixo para obter o FORMATO 2 (sem o campo cidade_nome)
    //@JsonIgnore
    public String getCidadeNome() {
        return cidade != null ? cidade.getNome() : null;
    }

    /**
     * Retorna o estado da cidade do cliente.
     * Este método é usado apenas para serialização JSON e não é persistido no banco de dados.
     *
     * Para implementar o FORMATO 2 (apenas o objeto cidade, sem campos cidade_*),
     * adicione a anotação @JsonIgnore a este método.
     *
     * @return O estado da cidade do cliente, ou null se o cliente não estiver associado a uma cidade
     */
    @JsonProperty("cidade_estado")
    // Descomente a linha abaixo para obter o FORMATO 2 (sem o campo cidade_estado)
    //@JsonIgnore
    public Estado getCidadeEstado() {
        return cidade != null ? cidade.getEstado() : null;
    }

    /**
     * Retorna o ID da cidade do cliente.
     * Este método é usado apenas para serialização JSON e não é persistido no banco de dados.
     *
     * Para implementar o FORMATO 2 (apenas o objeto cidade, sem campos cidade_*),
     * adicione a anotação @JsonIgnore a este método.
     *
     * @return O ID da cidade do cliente, ou null se o cliente não estiver associado a uma cidade
     */
    @JsonProperty("cidade_id")
    // Descomente a linha abaixo para obter o FORMATO 2 (sem o campo cidade_id)
    // @JsonIgnore
    public Long getCidadeId() {
        return cidade != null ? cidade.getId() : null;
    }
}
