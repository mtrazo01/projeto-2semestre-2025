package com.programacao.web.fatec.api_fatec.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Cidade;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Estado;
import java.util.List;

/**
 * Repositório para operações de banco de dados relacionadas à entidade Cliente.
 * Estende JpaRepository para herdar operações CRUD básicas.
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long>{
    /**
     * Busca clientes pelo nome.
     * 
     * @param nome O nome do cliente a ser buscado
     * @return Lista de clientes com o nome especificado
     */
    List<Cliente> findByNome(String nome);

    /**
     * Busca clientes pelo ID da cidade.
     * 
     * @param cidadeId O ID da cidade dos clientes a serem buscados
     * @return Lista de clientes da cidade especificada
     */
    @Query("select c from Cliente c where c.cidade.id = :cidadeId")
    List<Cliente> findByCidadeId(@Param("cidadeId") Long cidadeId);

    /**
     * Busca clientes por ID ou nome (parcial, case-insensitive).
     * 
     * @param id O ID do cliente a ser buscado
     * @param nome O nome (parcial) do cliente a ser buscado
     * @return Lista de clientes que correspondem ao ID ou nome
     */
    @Query("""
        select c from Cliente c 
        where 
            (c.id = :id) 
            OR (lower(c.nome) like lower(concat('%', :nome, '%')))
        """)
    List<Cliente> buscarPorIdOuNome(@Param("id") Long id, @Param("nome") String nome);

    /**
     * Busca clientes por texto que corresponda ao ID, nome ou nome da cidade.
     * Este método demonstra como trabalhar com relacionamentos em consultas SQL no PostgreSQL.
     * 
     * No banco dados relacional, os relacionamentos são implementados usando chaves estrangeiras.
     * A tabela 'clientes' tem uma coluna 'cidade_id' que referencia a tabela 'cidades'.
     * 
     * Na consulta SQL nativa, usamos JOIN para conectar as tabelas:
     * - c.* seleciona todos os campos da tabela clientes
     * - LEFT JOIN cidades cid ON c.cidade_id = cid.id conecta as tabelas
     * - Condições WHERE filtram os resultados
     * 
     * O parâmetro texto é usado para buscar em múltiplos campos:
     * - Se o texto puder ser convertido para Long, busca também pelo ID do cliente
     * - Busca por correspondência parcial no nome do cliente (case-insensitive)
     * - Busca por correspondência parcial no nome da cidade (case-insensitive)
     * 
     * @param texto O texto a ser buscado em múltiplos campos
     * @return Lista de clientes que correspondem aos critérios de busca
     */
    @Query(value = """
        SELECT c.* FROM clientes c
        LEFT JOIN cidades cid ON c.cidade_id = cid.id
        WHERE 
            (:idLong IS NOT NULL AND c.id = :idLong)
            OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(cid.nome) LIKE LOWER(CONCAT('%', :texto, '%'))
        """, nativeQuery = true)
    List<Cliente> buscarPorIdOuNomeComCidade(
        @Param("texto") String texto,
        @Param("idLong") Long idLong);
}
