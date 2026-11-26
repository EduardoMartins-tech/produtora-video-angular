package com.consumo.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePaginaController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/web/clientes";
    }
}
