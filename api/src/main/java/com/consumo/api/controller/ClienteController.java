package com.consumo.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consumo.api.entity.ClienteEntity;
import com.consumo.api.service.ClienteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/salvar")
    public ClienteEntity salvar(@RequestBody ClienteEntity cliente) {
        return clienteService.salvar(cliente);
    }

    @GetMapping("/buscar")
    public List<ClienteEntity> buscarTodos() {
        return clienteService.buscar();
    }

    @GetMapping({"/buscar/{id}", "/buscar{id}"})
    public ClienteEntity buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @GetMapping("/buscar/nome/{nome}")
    public List<ClienteEntity> buscarPorNome(@PathVariable String nome) {
        return clienteService.buscarPorNome(nome);
    }

    @PutMapping({"/atualizar/{id}", "/atualizar{id}"})
    public ClienteEntity atualizar(
            @PathVariable Integer id,
            @RequestBody ClienteEntity cliente) {
        return clienteService.atualizar(id, cliente);
    }

    @DeleteMapping({"/deletar/{id}", "/deletar{id}"})
    public void excluir(@PathVariable Integer id) {
        clienteService.excluir(id);
    }
}
