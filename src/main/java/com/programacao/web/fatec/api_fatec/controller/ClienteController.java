package com.programacao.web.fatec.api_fatec.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programacao.web.fatec.api_fatec.entities.Cliente;



@RestController
@RequestMapping("/api/cliente")
public class ClienteController {

    private final List<Cliente> listaDeCliente = new ArrayList<>();

    public ClienteController() {

        listaDeCliente.add (new Cliente(1L, "João"));
        Cliente cliente2 = new Cliente(2L, "Maria");
        cliente2.setId(2L);
        cliente2.setNome("Pedro");
        listaDeCliente.add(cliente2);

    }

    @GetMapping("/listarClientes")
    public List<Cliente> listarClientes() {
        return listaDeCliente;
    }

    @GetMapping("/testeCliente1")
    public String testeCliente() {
        return "Teste Cliente";
    }
    @GetMapping("/testeCliente2/{nome}")
    public String testeCliente2(@PathVariable String nome) {
        return nome;
    }
    @PostMapping("")
    public Cliente createCliente(@RequestBody Cliente cliente) {
        listaDeCliente.add(cliente);
        return cliente;
    }

    @DeleteMapping("/{id}")
    public String deletarCliente(@PathVariable Long id) {
        for (Cliente cliente : listaDeCliente) {
            if (cliente.getId().equals(id)) {
                listaDeCliente.remove(cliente);
                return "OK"; 
            }
        }
        return "Cliente não encontrado"; 
    }

    @PutMapping("/{id}")
    public String alterarCliente(@PathVariable Long id, @RequestBody Cliente entity) {
        for (Cliente cliente : listaDeCliente) {
            if (cliente.getId().equals(id)) {
                cliente.setId(id);
                cliente.setNome(entity.getNome());
                return "Encontrou";
            }
        }
        return "NÃO ENCONTRADO ID: " + id;
    }
        
}
