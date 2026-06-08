package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.comprovacao.ComprovacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdUpdateDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.TagPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.LeituraDeComprovacaoDeDeficienciaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.RegistroDeComprovacaoDeDeficienciaException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.FormularioRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.PcdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PcdService {
    private final PcdRepository repository;
    private final FormularioRepository formRepository;
    private final TagService tagService;
    private static final Path UPLOAD_PATH = Paths.get("upload");

    // CREATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public PcdResponseDto registrarPcd(PcdRequestDto requestDto) {
        UsuarioPcd pcdRegistrado = repository.findByEmail(requestDto.email())
                .map(pcd -> pcd.isAtivo() ? pcd : reativarPcd(pcd, requestDto))
                .orElseGet(() -> criarPcd(requestDto));

        return PcdResponseDto.fromEntity(repository.save(pcdRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<PcdResponseDto> listarPcdsAtivos() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(PcdResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'AGENTE_ATENDIMENTO') or authentication.name == #email")
    public PcdResponseDto buscarPcdAtivo(String email) {
        return PcdResponseDto.fromEntity(procurarPcdAtivo(email));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public ComprovacaoDto downloadComprovacao(String email) {
        UsuarioPcd pcd = procurarPcdAtivo(email);
        if (pcd.getComprovacaoId() == null) {
            throw new EntidadeNaoEncontradaException("Comprovacao", "pcd", email);
        }

        return carregarComprovacao(pcd.getComprovacaoId());
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public PcdResponseDto atualizarPcd(String email, PcdUpdateDto updateDto) {
        UsuarioPcd pcdAtualizado = procurarPcdAtivo(email);
        atualizarValores(pcdAtualizado, updateDto);
        return PcdResponseDto.fromEntity(repository.save(pcdAtualizado));
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public void removerPcd(String email) {
        UsuarioPcd pcdRemovido = procurarPcdAtivo(email);
        pcdRemovido.setAtivo(false);
        formRepository.findByEmailAndAtivoTrue(email)
                .ifPresent(form -> form.setAtivo(false));

        // Desvincula a tag, se houver
        TagPcd tagVinculada = pcdRemovido.getTag();
        if (tagVinculada != null) {
            tagVinculada.setUsuarioPcd(null);
            pcdRemovido.setTag(null);
        }

        repository.save(pcdRemovido);
    }

    // FUNÇÕES AUXILIARES
    protected String salvarComprovacao(MultipartFile comprovacao) {
        if (comprovacao == null || comprovacao.isEmpty()) {
            throw new LeituraDeComprovacaoDeDeficienciaException();
        }
        try {
            String originalFilename = comprovacao.getOriginalFilename();
            String extensao = (originalFilename != null && originalFilename.contains("."))
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";

            String comprovacaoId = UUID.randomUUID().toString();
            Files.createDirectories(UPLOAD_PATH);
            Path destino = UPLOAD_PATH.resolve(comprovacaoId + extensao);
            Files.write(destino, comprovacao.getBytes());
            return comprovacaoId;
        } catch (IOException e) {
            throw new RegistroDeComprovacaoDeDeficienciaException();
        }
    }

    public ComprovacaoDto carregarComprovacao(String comprovacaoId) {
        final Path filePath;

        try (Stream<Path> paths = Files.list(UPLOAD_PATH)) {
            filePath = paths
                    .filter(p -> p.getFileName().toString().startsWith(comprovacaoId))
                    .findFirst()
                    .orElseThrow(LeituraDeComprovacaoDeDeficienciaException::new);
        } catch (IOException e) {
            throw new LeituraDeComprovacaoDeDeficienciaException();
        }

        final Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new LeituraDeComprovacaoDeDeficienciaException();
        }

        if (!resource.exists() || !resource.isReadable()) {
            throw new LeituraDeComprovacaoDeDeficienciaException();
        }

        final String contentType;
        try {
            contentType = Optional.ofNullable(Files.probeContentType(filePath))
                    .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        } catch (IOException e) {
            throw new LeituraDeComprovacaoDeDeficienciaException();
        }

        return new ComprovacaoDto(resource, contentType, comprovacaoId);
    }

    public void criarPcdPorFormulario(FormularioPcd formulario) {
        UsuarioPcd pcd = new UsuarioPcd();
        pcd.setNome(formulario.getNome());
        pcd.setEmail(formulario.getEmail());
        pcd.setSenha(formulario.getSenha());
        pcd.setTiposDeficiencia(new HashSet<>(formulario.getTiposDeficiencia()));
        pcd.setDesejaSuporte(formulario.isDesejaSuporte());
        pcd.setComprovacaoId(formulario.getComprovacaoId());
        pcd.setTag(tagService.escolherTagDisponivel());
        pcd.setRole(Role.USUARIO_PCD);
        pcd.setAtivo(true);
        repository.save(pcd);
    }

    protected UsuarioPcd procurarPcdAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("UsuarioPcd", "email", email));
    }

    protected boolean existePcdAtivo(String email) {
        return repository.existsByEmailAndAtivoTrue(email);
    }

    private UsuarioPcd reativarPcd(UsuarioPcd pcd, PcdRequestDto requestDto) {
        pcd.setAtivo(true);
        pcd.setNome(requestDto.nome());
        pcd.setEmail(requestDto.email());
        pcd.setSenha(requestDto.senha());
        pcd.setTag(tagService.escolherTagDisponivel());
        pcd.setTiposDeficiencia(requestDto.tiposDeficiencia());
        pcd.setDesejaSuporte(requestDto.desejaSuporte());
        pcd.setComprovacaoId(salvarComprovacao(requestDto.comprovacao()));
        return pcd;
    }

    private UsuarioPcd criarPcd(PcdRequestDto requestDto) {
        TagPcd tagPcd = tagService.escolherTagDisponivel();
        String comprovacaoId = salvarComprovacao(requestDto.comprovacao());
        UsuarioPcd pcd = requestDto.toEntity(comprovacaoId, tagPcd);
        pcd.setTag(tagPcd);
        pcd.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return pcd;
    }

    private void atualizarValores(UsuarioPcd pcd, PcdUpdateDto updateDto) {
        if (updateDto.nome() != null)
            pcd.setNome(updateDto.nome());
        if (updateDto.email() != null)
            pcd.setEmail(updateDto.email());
        if (updateDto.senha() != null)
            pcd.setSenha(updateDto.senha());
        if (updateDto.tiposDeficiencia() != null)
            pcd.setTiposDeficiencia(updateDto.tiposDeficiencia());
        if (updateDto.desejaSuporte() != null)
            pcd.setDesejaSuporte(updateDto.desejaSuporte());
        if (updateDto.comprovacao() != null && !updateDto.comprovacao().isEmpty())
            pcd.setComprovacaoId(salvarComprovacao(updateDto.comprovacao()));
    }
}