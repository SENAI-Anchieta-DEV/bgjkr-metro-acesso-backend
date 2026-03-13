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

    // READ
    @Transactional(readOnly = true)
    public List<TagResponseDto> listarTagsAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(TagResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public TagResponseDto buscarTagAtiva(String codigoTag) {
        return TagResponseDto.fromEntity(procurarTagAtiva(codigoTag));
    }

    // UPDATE
    @Transactional
    public TagResponseDto atualizarTag(String codigoTag, TagRequestDto requestDto) {
        TagPcd tagAtualizada = procurarTagAtiva(codigoTag);

        atualizarValores(tagAtualizada, requestDto);

        return TagResponseDto.fromEntity(repository.save(tagAtualizada));
    }

    // DELETE
    @Transactional
    public void removerTag(String codigoTag) {
        TagPcd tagRemovida = procurarTagAtiva(codigoTag);

        tagRemovida.setAtivo(false);

        repository.save(tagRemovida);
    }

    // Funções auxiliares
    private TagPcd reativarTag(TagPcd tag, TagRequestDto requestDto) {
        atualizarValores(tag, requestDto);
        tag.setAtivo(true);
        return tag;
    }

    private TagPcd criarTag(TagRequestDto requestDto) {
        return requestDto.toEntity();
    }

    protected TagPcd procurarTagAtiva(String codigoTag) {
        return repository
                .findByCodigoTagAndAtivoTrue(codigoTag)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }

    private void atualizarValores(TagPcd tag, TagRequestDto requestDto) {
        tag.setCodigoTag(requestDto.codigoTag());
    }
}
