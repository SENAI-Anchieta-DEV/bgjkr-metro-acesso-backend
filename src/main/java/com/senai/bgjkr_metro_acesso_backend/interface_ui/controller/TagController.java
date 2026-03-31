package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

   // CREATE
    @PostMapping
    public ResponseEntity<TagResponseDto> registrarTag(@RequestBody @Valid TagRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/tag"))
                .body(service.registrarTag(requestDto));
    }

    // READ
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> listarTagsAtivas() {
        return ResponseEntity
                .ok(service.listarTagsAtivas());
    }

    @GetMapping("/{codigoTag}")
    public ResponseEntity<TagResponseDto> buscarTagAtiva(@PathVariable String codigoTag) {
        return ResponseEntity
                .ok(service.buscarTagAtiva(codigoTag));
    }

    // UPDATE
    @PutMapping("/{codigoTag}")
    public ResponseEntity<TagResponseDto> atualizarTag(@PathVariable String codigoTag, @RequestBody @Valid TagRequestDto requestDto) {
        return ResponseEntity
                .ok(service.atualizarTag(codigoTag, requestDto));
    }

    // DELETE
    @DeleteMapping("/{codigoTag}")
    public ResponseEntity<Void> removerTag(@PathVariable String codigoTag) {
        service.removerTag(codigoTag);
        return ResponseEntity
                .noContent()
                .build();
    }
}

