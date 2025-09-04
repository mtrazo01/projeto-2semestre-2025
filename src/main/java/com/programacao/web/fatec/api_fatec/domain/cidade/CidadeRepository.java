package com.programacao.web.fatec.api_fatec.domain.cidade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programacao.web.fatec.api_fatec.entities.Cidade;
import com.programacao.web.fatec.api_fatec.entities.Estado;

import java.util.List;

/**
 * Repositório para operações de banco de dados relacionadas à entidade Cidade.
 * Estende JpaRepository para herdar operações CRUD básicas.
 */
public interface CidadeRepository extends JpaRepository<Cidade, Long> {

}