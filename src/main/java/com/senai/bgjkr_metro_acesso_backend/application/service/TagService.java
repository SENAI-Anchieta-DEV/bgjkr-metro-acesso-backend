package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.tag_pcd.TagIndisponivelException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository repository;

    // CREATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TagResponseDto registrarTag(TagRequestDto requestDto) {
        TagPcd tagRegistrado = repository.findByCodigoTag(requestDto.codigoTag())
                .map(tag -> tag.isAtivo() ? tag : reativarTag(tag, requestDto))
                .orElseGet(() -> criarTag(requestDto));

        return TagResponseDto.fromEntity(repository.save(tagRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<TagResponseDto> listarTagsAtivas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(TagResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TagResponseDto buscarTagAtiva(String codigoTag) {
        return TagResponseDto.fromEntity(procurarTagAtiva(codigoTag));
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public TagResponseDto atualizarTag(String codigoTag, TagRequestDto requestDto) {
        TagPcd tagAtualizada = procurarTagAtiva(codigoTag);
        atualizarValores(tagAtualizada, requestDto);
        return TagResponseDto.fromEntity(repository.save(tagAtualizada));
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerTag(String codigoTag) {
        TagPcd tagRemovida = procurarTagAtiva(codigoTag);
        tagRemovida.setAtivo(false);

        UsuarioPcd pcdVinculado = tagRemovida.getUsuarioPcd();
        if (pcdVinculado != null) {
            pcdVinculado.setTag(null);
            tagRemovida.setUsuarioPcd(null);
        }

        repository.save(tagRemovida);
    }

    // Funções auxiliares
    protected TagPcd escolherTagDisponivel() {
        return repository
                .findFirstByUsuarioPcdNull()
                .orElseThrow(TagIndisponivelException::new);
    }

    protected TagPcd procurarTagAtiva(String codigoTag) {
        return repository
                .findByCodigoTagAndAtivoTrue(codigoTag)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("TagPcd", "codigoTag", codigoTag));
    }

    private TagPcd reativarTag(TagPcd tag, TagRequestDto requestDto) {
        atualizarValores(tag, requestDto);
        tag.setAtivo(true);
        return tag;
    }

    private TagPcd criarTag(TagRequestDto requestDto) {
        return requestDto.toEntity();
    }

    private void atualizarValores(TagPcd tag, TagRequestDto requestDto) {
        tag.setCodigoTag(requestDto.codigoTag());
    }
}
