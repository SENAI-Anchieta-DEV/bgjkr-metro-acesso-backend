package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagEventoDTO;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagEventoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tag")
public class TagEventoController {

    private final TagEventoService service;

    public TagEventoController(TagEventoService service) {
        this.service = service;
    }

    @GetMapping("/ultimo")
    public TagEventoDTO getUltimoEvento() {
        return service.getUltimoEvento();
    }
}