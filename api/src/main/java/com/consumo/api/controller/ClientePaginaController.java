package com.consumo.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.consumo.api.entity.ClienteEntity;
import com.consumo.api.service.ClienteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/clientes")
@RequiredArgsConstructor
public class ClientePaginaController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.buscar());
        return "clientes";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new ClienteEntity());
        return "cliente-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        model.addAttribute("cliente", clienteService.buscarPorId(id));
        return "cliente-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ClienteEntity cliente) {
        try {
            if (cliente.getIdCliente() == null) {
                clienteService.salvar(cliente);
            } else {
                clienteService.atualizar(cliente.getIdCliente(), cliente);
            }
        } catch (Exception e) {
            // dado inválido ou repetido: não grava e volta para a lista
        }
        return "redirect:/web/clientes";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            clienteService.excluir(id);
        } catch (Exception e) {
            // cliente com projeto vinculado não pode ser excluído
        }
        return "redirect:/web/clientes";
    }
}
