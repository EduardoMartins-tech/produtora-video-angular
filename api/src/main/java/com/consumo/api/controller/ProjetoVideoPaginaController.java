package com.consumo.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.consumo.api.entity.ProjetoVideoEntity;
import com.consumo.api.service.ClienteService;
import com.consumo.api.service.ProjetoVideoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/projetos")
@RequiredArgsConstructor
public class ProjetoVideoPaginaController {

    private final ProjetoVideoService projetoVideoService;
    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("projetos", projetoVideoService.buscar());
        return "projetos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("projeto", new ProjetoVideoEntity());
        model.addAttribute("clientes", clienteService.buscar());
        return "projeto-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("projeto", projetoVideoService.buscarPorId(id));
        model.addAttribute("clientes", clienteService.buscar());
        return "projeto-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ProjetoVideoEntity projeto) {
        try {
            if (projeto.getIdProjeto() == null) {
                projetoVideoService.salvar(projeto);
            } else {
                projetoVideoService.atualizar(projeto.getIdProjeto(), projeto);
            }
        } catch (Exception e) {
            // dado inválido ou repetido: não grava e volta para a lista
        }
        return "redirect:/web/projetos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            projetoVideoService.excluir(id);
        } catch (Exception e) {
            // projeto com entrega vinculada não pode ser excluído
        }
        return "redirect:/web/projetos";
    }
}
