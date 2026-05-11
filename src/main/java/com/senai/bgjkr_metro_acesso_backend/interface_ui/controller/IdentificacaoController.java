package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/iot")
public class IdentificacaoController {
    private final IdentificacaoService service;

    public IdentificacaoController(IdentificacaoService service) {
        this.service = service;
    }

    @PostMapping("/simular")
    public ResponseEntity<String> processarEvento(@RequestBody String payload) {
        return ResponseEntity
                .created(URI.create("iot/simular"))
                .body(service.processarEvento(payload));
    }

    @GetMapping("/ultimo-evento")
    public ResponseEntity<String> getUltimoEvento() {
        return ResponseEntity.ok(service.getUltimoEvento());
    }
}
