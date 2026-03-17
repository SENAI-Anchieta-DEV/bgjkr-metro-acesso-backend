package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PcdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor

public class PcdService {
    private final PcdRepository repository;
    private final TagService tagService;

    // CREATE
    @Transactional
    public PcdResponseDto registrarPcd(PcdRequestDto requestDto) {
        UsuarioPcd pcdRegistrado = repository.findByEmail(requestDto.email())
                .map(pcd -> pcd.isAtivo() ? pcd : reativarPcd(pcd, requestDto))
                .orElseGet(() -> criarPcd(requestDto));

        return PcdResponseDto.fromEntity(repository.save(pcdRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    public List<PcdResponseDto> listarPcdsAtivos() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(PcdResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public PcdResponseDto buscarPcdAtivo(String email) {
        return PcdResponseDto.fromEntity(procurarPcdAtivo(email));
    }

    // UPDATE
    @Transactional
    public PcdResponseDto atualizarPcd(String email, PcdRequestDto requestDto) {
        UsuarioPcd pcdAtualizado = procurarPcdAtivo(email);
        atualizarValores(pcdAtualizado, requestDto);
        return PcdResponseDto.fromEntity(repository.save(pcdAtualizado));
    }

    // DELETE
    @Transactional
    public void removerPcd(String email) {
        UsuarioPcd pcdRemovido = procurarPcdAtivo(email);
        pcdRemovido.setAtivo(false);

        TagPcd tagVinculada = pcdRemovido.getTag();
        if (tagVinculada != null) {
            tagVinculada.setUsuarioPcd(null);
            pcdRemovido.setTag(null);
        }

        repository.save(pcdRemovido);
    }

    // Funções auxiliares
    protected UsuarioPcd procurarPcdAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }

    private UsuarioPcd reativarPcd(UsuarioPcd pcd, PcdRequestDto requestDto) {
        atualizarValores(pcd, requestDto);
        pcd.setAtivo(true);
        return pcd;
    }

    private UsuarioPcd criarPcd(PcdRequestDto requestDto) {
        TagPcd tagPcd = tagService.procurarTagAtiva(requestDto.codigoTag());
        UsuarioPcd pcd = requestDto.toEntity(tagPcd);
        pcd.setTag(tagPcd);
        pcd.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return pcd;
    }

    private void atualizarValores(UsuarioPcd pcd, PcdRequestDto requestDto) {
        pcd.setNome(requestDto.nome());
        pcd.setEmail(requestDto.email());
        pcd.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        pcd.setTag(tagService.procurarTagAtiva(requestDto.codigoTag()));
        pcd.setTiposDeficiencia(requestDto.tiposDeficiencia());
        pcd.setDesejaSuporte(requestDto.desejaSuporte());
    }
}
