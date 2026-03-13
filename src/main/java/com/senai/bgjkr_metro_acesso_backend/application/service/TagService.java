package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class TagService {
    private final TagRepository repository;

    // CREATE
    @Transactional
    public TagResponseDto registrarTag(TagRequestDto requestDto) {
        TagPcd tagRegistrado = repository.findByCodigoTag(requestDto.codigoTag())
                .map(tag -> tag.isAtivo() ? tag : reativarTag(tag, requestDto))
                .orElseGet(() -> criarTag(requestDto));

        return TagResponseDto.fromEntity(repository.save(tagRegistrado));
    }
}
