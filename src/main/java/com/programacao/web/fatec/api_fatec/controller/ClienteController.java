package com.programacao.web.fatec.api_fatec.controller;

import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.domain.cidade.CidadeRepository;
import com.programacao.web.fatec.api_fatec.domain.cliente.ClienteRepository;
import com.programacao.web.fatec.api_fatec.domain.cliente.dto.BuscaPorIdOuNomeDto;
import com.programacao.web.fatec.api_fatec.entities.Cidade;
import com.programacao.web.fatec.api_fatec.entities.Cliente;
import com.programacao.web.fatec.api_fatec.entities.Estado;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

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
    @PostConstruct()
    public void dadosIniciais() {
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

    @GetMapping("/listarClientes")
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/buscaPorIdOuNome/{search}")
    public List<Cliente> buscaPorIdOuNomeGenerico(@PathVariable String search) {
        Long id = null;
        try {
            id = Long.parseLong(search);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(e.getMessage());
        }

        return clienteRepository.buscarPorIdOuNome(id, search);
    }

    @PostMapping("/buscaPorIdOuNome")
    public List<Cliente> buscaPorIdOuNome(@RequestBody BuscaPorIdOuNomeDto dto) {
        return clienteRepository.buscarPorIdOuNome(dto.getId(), dto.getNome());
    }

    /**
     * Busca clientes por texto que corresponda ao ID, nome ou nome da cidade.
     * Este método demonstra como trabalhar com relacionamentos em consultas SQL em Banco dados relacional.
     * 
     * O parâmetro texto é usado para buscar em múltiplos campos:
     * - Se o texto puder ser convertido para Long, busca também pelo ID do cliente
     * - Busca por correspondência parcial no nome do cliente (case-insensitive)
     * - Busca por correspondência parcial no nome da cidade (case-insensitive)
     * 
     * @param texto O texto a ser buscado em múltiplos campos
     * @return Lista de clientes que correspondem aos critérios de busca
     */
    @GetMapping("/buscarPorTexto")
    public List<Cliente> buscarPorTexto(@RequestParam String texto) {
        // Tenta converter o texto para Long para buscar por ID
        Long idLong = null;
        try {
            idLong = Long.parseLong(texto);
        } catch (NumberFormatException e) {
            // Se não for possível converter para Long, deixa idLong como null
            System.out.println("Texto não pode ser convertido para Long: " + texto);
        }

        // Chama o método do repositório com o texto e o possível ID
        return clienteRepository.buscarPorIdOuNomeComCidade(texto, idLong);
    }

    /**
     * Cria um novo cliente.
     * Se o cliente tiver uma cidade associada (com ID), busca a cidade no banco de dados.
     * 
     * @param cliente Dados do cliente a ser criado
     * @return Cliente criado com ID gerado
     */
    @PostMapping("")
    public Cliente createCliente(@RequestBody Cliente cliente) {
        cliente.setId(null);

        // Verifica se o cliente tem uma cidade associada
        if (cliente.getCidade() != null && cliente.getCidade().getId() != null) {
            // Busca a cidade pelo ID
            Optional<Cidade> cidadeOpt = cidadeRepository.findById(cliente.getCidade().getId());

            // Se a cidade existir, associa ao cliente
            if (cidadeOpt.isPresent()) {
                cliente.setCidade(cidadeOpt.get());
            } else {
                // Se a cidade não existir, remove a associação
                cliente.setCidade(null);
            }
        }

        Cliente clienteCreated = clienteRepository.save(cliente);
        return clienteCreated;
    }

    @DeleteMapping("/{id}")
    public String deletarCliente(@PathVariable Long id) {
        //1º exemplo 
        //clienteRepository.deleteById(id);

        //2 exemplo
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(id);
        if (clienteEncontrado.isPresent()) {
            clienteRepository.deleteById(id);
            return "Cliente Deletado";
        }

        return "NÃO ENCONTRADO ID:"+id;
    }

    /**
     * Atualiza um cliente existente.
     * Se o cliente tiver uma cidade associada (com ID), busca a cidade no banco de dados.
     * 
     * @param id ID do cliente a ser atualizado
     * @param entity Novos dados do cliente
     * @return Mensagem indicando o resultado da operação
     */
    @PutMapping("/{id}")
    public String alterarCliente(@PathVariable Long id, @RequestBody Cliente entity) {
        Optional<Cliente> clienteEncontrado = clienteRepository.findById(id);
        if (!clienteEncontrado.isPresent()) {
            return String.format("NÃO ENCONTRADO ID: %s", id);
        }

        entity.setId(id);

        // Verifica se o cliente tem uma cidade associada
        if (entity.getCidade() != null && entity.getCidade().getId() != null) {
            // Busca a cidade pelo ID
            Optional<Cidade> cidadeOpt = cidadeRepository.findById(entity.getCidade().getId());

            // Se a cidade existir, associa ao cliente
            if (cidadeOpt.isPresent()) {
                entity.setCidade(cidadeOpt.get());
            } else {
                // Se a cidade não existir, remove a associação
                entity.setCidade(null);
            }
        }

        clienteRepository.save(entity);
        return "Cliente Atualizado com sucesso";
    }

}
