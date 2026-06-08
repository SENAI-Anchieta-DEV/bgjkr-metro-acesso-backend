package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.comprovacao.ComprovacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.EntidadeNaoEncontradaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.auth.UsuarioNaoAutenticadoException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.AlterarFormularioPcdJaValidadoException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.ComprovacaoDeDeficienciaAusenteException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.FormularioPcdComEmailDeUsuarioAtivoException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.LeituraDeComprovacaoDeDeficienciaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.MotivoReprovacaoAusenteException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.RegistroDeComprovacaoDeDeficienciaException;
import com.senai.bgjkr_metro_acesso_backend.domain.exception.formulario.RemocaoDeFormularioDePcdAtivoException;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.FormularioRepository;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FormularioService {
    private final FormularioRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final AdminService adminService;
    private final PcdService pcdService;
    private static final Path UPLOAD_PATH = Paths.get("upload");

    // CREATE
    @Transactional
    public FormSolicitacaoResponseDto registrarFormulario(FormSolicitacaoRequestDto requestDto) {
        impedirEmailAtivo(requestDto.email());
        impedirFormularioNaoComprovado(requestDto.comprovacao());
        FormularioPcd formRegistrado = repository.findByEmail(requestDto.email())
                .map(form -> reativarFormulario(requestDto, form))
                .orElseGet(() -> criarFormulario(requestDto));

        return FormSolicitacaoResponseDto.fromEntity(repository.save(formRegistrado));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<FormAprovacaoResponseDto> listarFormulariosAtivos() {
        return repository.findAllByAtivoTrue()
                .stream()
                .map(FormAprovacaoResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<FormSolicitacaoResponseDto> listarFormulariosPendentes() {
        return repository.findAllByStatusAndAtivoTrue(StatusFormulario.EM_ANALISE)
                .stream()
                .map(FormSolicitacaoResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public FormAprovacaoResponseDto buscarFormularioAtivo(String email) {
        return FormAprovacaoResponseDto.fromEntity(procurarFormularioAtivo(email));
    }

    // UPDATE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public FormSolicitacaoResponseDto atualizarFormularioPendente(String email, FormSolicitacaoRequestDto requestDto) {
        impedirEmailAtivo(requestDto.email());
        impedirFormularioNaoComprovado(requestDto.comprovacao());
        FormularioPcd formAtualizado = procurarFormularioAtivo(email);
        impedirFormularioJaValidado(formAtualizado);
        atualizarSolicitacao(formAtualizado, requestDto);
        return FormSolicitacaoResponseDto.fromEntity(formAtualizado);
    }

    // DELETE
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void removerFormularioPendente(String email) {
        impedirPcdAtivo(email);
        FormularioPcd formRemovido = procurarFormularioAtivo(email);
        formRemovido.setAtivo(false);
    }

    // OUTRAS FUNCIONALIDADES
    @Transactional
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public FormAprovacaoResponseDto validarFormulario(String email, FormAprovacaoRequestDto requestDto) {
        impedirMotivoInvalido(requestDto);
        FormularioPcd formValidado = procurarFormularioAtivo(email);
        impedirFormularioJaValidado(formValidado);
        Administrador adminResponsavel = buscarAdministradorResponsavel();
        atualizarValidacao(formValidado, requestDto, adminResponsavel);
        solicitarNovoPcdAprovado(formValidado, requestDto.aprovado());
        return FormAprovacaoResponseDto.fromEntity(formValidado);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMINISTRADOR') or authentication.name == #email")
    public ComprovacaoDto downloadComprovacao(String email) {
        FormularioPcd formulario = procurarFormularioAtivo(email);
        if (formulario.getComprovacaoId() == null) {
            throw new EntidadeNaoEncontradaException("Comprovacao", "formulario", email);
        }

        return carregarComprovacao(formulario.getComprovacaoId());
    }

    // Funções auxiliares
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

    protected void impedirEmailAtivo(String email) {
        if (usuarioRepository.existsByEmailAndAtivoTrue(email)) {
            throw new FormularioPcdComEmailDeUsuarioAtivoException();
        }
    }

    private void impedirFormularioNaoComprovado(MultipartFile comprovacao) {
        if (comprovacao == null || comprovacao.isEmpty()) {
            throw new ComprovacaoDeDeficienciaAusenteException();
        }
    }

    private void impedirFormularioJaValidado(FormularioPcd formulario) {
        if (formulario.getStatus() != StatusFormulario.EM_ANALISE) {
            throw new AlterarFormularioPcdJaValidadoException();
        }
    }

    private void impedirPcdAtivo(String email) {
        if (pcdService.existePcdAtivo(email)) {
            throw new RemocaoDeFormularioDePcdAtivoException();
        }
    }

    private void impedirMotivoInvalido(FormAprovacaoRequestDto requestDto) {
        if (!requestDto.aprovado() && (requestDto.motivoReprovacao() == null || requestDto.motivoReprovacao().isBlank())) {
            throw new MotivoReprovacaoAusenteException();
        }
    }

    private FormularioPcd procurarFormularioAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("FormularioPcd", "email", email));
    }

    private FormularioPcd reativarFormulario(FormSolicitacaoRequestDto requestDto, FormularioPcd formExistente) {
        if (formExistente.getStatus() != StatusFormulario.APROVADO || !formExistente.isAtivo()) {
            atualizarSolicitacao(formExistente, requestDto);
            formExistente.setStatus(StatusFormulario.EM_ANALISE);
            formExistente.setAdminResponsavel(null);
            formExistente.setMotivoReprovacao(null);
            formExistente.setAtivo(true);
        }
        return formExistente;
    }

    private FormularioPcd criarFormulario(FormSolicitacaoRequestDto requestDto) {
        String comprovacaoId = salvarComprovacao(requestDto.comprovacao());
        FormularioPcd formulario = requestDto.toEntity(comprovacaoId);
        formulario.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return formulario;
    }

    private void atualizarSolicitacao(FormularioPcd formulario, FormSolicitacaoRequestDto requestDto) {
        formulario.setNome(requestDto.nome());
        formulario.setEmail(requestDto.email());
        formulario.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        formulario.setTiposDeficiencia(requestDto.tiposDeficiencia());
        formulario.setDesejaSuporte(requestDto.desejaSuporte());
        formulario.setComprovacaoId(salvarComprovacao(requestDto.comprovacao()));
    }

    private void atualizarValidacao(FormularioPcd formulario, FormAprovacaoRequestDto requestDto, Administrador adminResponsavel) {
        formulario.setStatus(requestDto.aprovado() ? StatusFormulario.APROVADO : StatusFormulario.REPROVADO);
        formulario.setMotivoReprovacao(requestDto.motivoReprovacao());
        formulario.setAdminResponsavel(adminResponsavel);
    }

    private Administrador buscarAdministradorResponsavel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UsuarioNaoAutenticadoException();
        }
        return adminService.procurarAdminAtivo(auth.getName());
    }

    private void solicitarNovoPcdAprovado(FormularioPcd formulario, boolean aprovado) {
        if (aprovado) {
            pcdService.criarPcdPorFormulario(formulario);
        }
    }
}