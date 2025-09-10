package com.programacao.web.fatec.api_fatec.domain.dados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.programacao.web.fatec.api_fatec.domain.cidade.CidadeRepository;
import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.entities.Cidade;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Estado;

import jakarta.annotation.PostConstruct;

/**
 * Serviço responsável pela inicialização de dados para demonstração.
 * Cria dados iniciais como cidades e clientes ao iniciar a aplicação.
 */
@Service
public class DadosIniciaisService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    /**
     * Método executado após a inicialização do contexto do Spring.
     * Cria dados iniciais para demonstração, incluindo cidades e clientes.
     * 
     * Relacionamento 1:N entre Cidade e Cliente:
     * - Uma cidade pode ter vários clientes (1:N)
     * - Um cliente pertence a uma única cidade (N:1)
     */
    @PostConstruct
    public void inicializarDados() {
        // Criando 5 cidades de exemplo com diferentes estados
        Cidade saoPaulo = cidadeRepository.save(new Cidade(null, "São Paulo", Estado.SP));
        Cidade rioDeJaneiro = cidadeRepository.save(new Cidade(null, "Rio de Janeiro", Estado.RJ));
        Cidade beloHorizonte = cidadeRepository.save(new Cidade(null, "Belo Horizonte", Estado.MG));
        Cidade salvador = cidadeRepository.save(new Cidade(null, "Salvador", Estado.BA));
        Cidade fortaleza = cidadeRepository.save(new Cidade(null, "Fortaleza", Estado.CE));

        // Criando clientes associados às cidades
        // Observe como o relacionamento é estabelecido passando a cidade como parâmetro
        clienteRepository.save(new Cliente(null, "Danilo", "Av. Paulista, 1000", saoPaulo));
        clienteRepository.save(new Cliente(null, "Maria", "Rua Copacabana, 500", rioDeJaneiro));
        clienteRepository.save(new Cliente(null, "João", "Av. Afonso Pena, 123", beloHorizonte));
        clienteRepository.save(new Cliente(null, "Ana", "Rua Chile, 45", salvador));
        clienteRepository.save(new Cliente(null, "Pedro", "Av. Beira Mar, 789", fortaleza));
    }
}