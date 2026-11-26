package com.consumo.api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consumo.api.entity.ProjetoVideoEntity;
import com.consumo.api.service.ProjetoVideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
public class ProjetoVideoController {

    private final ProjetoVideoService projetoVideoService;

    @PostMapping("/salvar")
    public ProjetoVideoEntity salvar(@RequestBody ProjetoVideoEntity projeto) {
        return projetoVideoService.salvar(projeto);
    }

    @GetMapping("/buscar")
    public List<ProjetoVideoEntity> buscarTodos() {
        return projetoVideoService.buscar();
    }

    @GetMapping({"/buscar/{id}", "/buscar{id}"})
    public ProjetoVideoEntity buscarPorId(@PathVariable Integer id) {
        return projetoVideoService.buscarPorId(id);
    }

    @GetMapping("/buscar/orcamento/{valorMinimo}")
    public List<ProjetoVideoEntity> buscarPorOrcamentoMinimo(@PathVariable BigDecimal valorMinimo) {
        return projetoVideoService.buscarPorOrcamentoMinimo(valorMinimo);
    }

    @PutMapping({"/atualizar/{id}", "/atualizar{id}"})
    public ProjetoVideoEntity atualizar(
            @PathVariable Integer id,
            @RequestBody ProjetoVideoEntity projeto) {
        return projetoVideoService.atualizar(id, projeto);
    }

    @DeleteMapping({"/deletar/{id}", "/deletar{id}"})
    public void excluir(@PathVariable Integer id) {
        projetoVideoService.excluir(id);
    }
}
