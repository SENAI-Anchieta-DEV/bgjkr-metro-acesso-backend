package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @PostMapping
    public TagResponseDto registrarTag(@RequestBody TagRequestDto requestDto) {
        return service.registrarTag(requestDto);
}

    // READ
    @GetMapping
    public List<TagResponseDto> listarTagsAtivas() {
        return service.listarTagsAtivas();
    }
    @GetMapping("/{codigoTag}")
    public TagResponseDto buscarTagAtiva(@PathVariable String codigoTag) {
        return service.buscarTagAtiva(codigoTag);
    }

}

