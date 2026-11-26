package com.consumo.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.consumo.api.entity.EntregaProjetoEntity;
import com.consumo.api.service.EntregaProjetoService;
import com.consumo.api.service.ProjetoVideoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/entregas")
@RequiredArgsConstructor
public class EntregaProjetoPaginaController {

    private final EntregaProjetoService entregaProjetoService;
    private final ProjetoVideoService projetoVideoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("entregas", entregaProjetoService.buscar());
        return "entregas";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("entrega", new EntregaProjetoEntity());
        model.addAttribute("projetos", projetoVideoService.buscar());
        return "entrega-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("entrega", entregaProjetoService.buscarPorId(id));
        model.addAttribute("projetos", projetoVideoService.buscar());
        return "entrega-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute EntregaProjetoEntity entrega) {
        try {
            if (entrega.getIdEntrega() == null) {
                entregaProjetoService.salvar(entrega);
            } else {
                entregaProjetoService.atualizar(entrega.getIdEntrega(), entrega);
            }
        } catch (Exception e) {
            // dado inválido ou repetido: não grava e volta para a lista
        }
        return "redirect:/web/entregas";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            entregaProjetoService.excluir(id);
        } catch (Exception e) {
        }
        return "redirect:/web/entregas";
    }
}
