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

import com.consumo.api.entity.EntregaProjetoEntity;
import com.consumo.api.service.EntregaProjetoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaProjetoController {

    private final EntregaProjetoService entregaProjetoService;

    @PostMapping("/salvar")
    public EntregaProjetoEntity salvar(@RequestBody EntregaProjetoEntity entrega) {
        return entregaProjetoService.salvar(entrega);
    }

    @GetMapping("/buscar")
    public List<EntregaProjetoEntity> buscarTodos() {
        return entregaProjetoService.buscar();
    }

    @GetMapping({"/buscar/{id}", "/buscar{id}"})
    public EntregaProjetoEntity buscarPorId(@PathVariable Integer id) {
        return entregaProjetoService.buscarPorId(id);
    }

    @GetMapping("/buscar/status/{status}")
    public List<EntregaProjetoEntity> buscarPorStatus(@PathVariable String status) {
        return entregaProjetoService.buscarPorStatus(status);
    }

    @PutMapping({"/atualizar/{id}", "/atualizar{id}"})
    public EntregaProjetoEntity atualizar(
            @PathVariable Integer id,
            @RequestBody EntregaProjetoEntity entrega) {
        return entregaProjetoService.atualizar(id, entrega);
    }

    @DeleteMapping({"/deletar/{id}", "/deletar{id}"})
    public void excluir(@PathVariable Integer id) {
        entregaProjetoService.excluir(id);
    }
}
