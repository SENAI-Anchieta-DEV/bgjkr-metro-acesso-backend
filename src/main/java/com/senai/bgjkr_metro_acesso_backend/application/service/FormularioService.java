package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import com.senai.bgjkr_metro_acesso_backend.domain.repository.FormularioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FormularioService {
    private final FormularioRepository repository;
    private final AdminService adminService;
    private final TagService tagService;
    private final PcdService pcdService;
    private final UsuarioService usuarioService;

    // CREATE
    @Transactional
    public FormSolicitacaoResponseDto enviarFormulario(FormSolicitacaoRequestDto requestDto) {
        FormularioPcd formRegistrado = criarFormulario(requestDto);
        return FormSolicitacaoResponseDto.fromEntity(repository.save(formRegistrado));
    }

    // READ
    @Transactional
    public FormSolicitacaoResponseDto buscarFormularioAtivo(String email) {
        return FormSolicitacaoResponseDto.fromEntity(procurarFormularioAtivo(email));
    }

    // UPDATE
    @Transactional
    public FormSolicitacaoResponseDto atualizarFormulario(String email, FormSolicitacaoRequestDto requestDto) {
        FormularioPcd formAtualizado = procurarFormularioAtivo(email);
        atualizarSolicitacao(formAtualizado, requestDto);
        return FormSolicitacaoResponseDto.fromEntity(formAtualizado);
    }

    // DELETE
    @Transactional
    public void removerFormulario(String email) {
        FormularioPcd formAtivo = procurarFormularioAtivo(email);
        formAtivo.setAtivo(false);
    }

    @Transactional
    public FormAprovacaoResponseDto validarFormulario(String email, FormAprovacaoRequestDto requestDto) {
        exigirMotivoReprovacao(requestDto);
        FormularioPcd formValidado = procurarFormularioAtivo(email);
        Administrador adminResponsavel = buscarAdministradorResponsavel();

        atualizarAprovacao(formValidado, requestDto, adminResponsavel);
        if (requestDto.aprovado()) {
            solicitarNovoPcd(email, formValidado);
        }
        return FormAprovacaoResponseDto.fromEntity(formValidado);
    }

    private void exigirMotivoReprovacao(FormAprovacaoRequestDto requestDto) {
        if (!requestDto.aprovado() && (requestDto.motivoReprovacao() == null || requestDto.motivoReprovacao().isBlank())) {
            throw new IllegalArgumentException("Motivo é obrigatório para reprovação"); // Exception específica em futura feature
        }
    }

    private void solicitarNovoPcd(String email, FormularioPcd formulario) {
        pcdService.registrarPcd(new PcdRequestDto(
                formulario.getNome(),
                email,
                formulario.getSenha(),
                new HashSet<>(formulario.getTiposDeficiencia()),
                formulario.isDesejaSuporte(),
                tagService.escolherTagDisponivel().getCodigoTag()
        ));
    }

    private void verificarFormularioExistente(String email) {
        if (repository.findByEmailAndAtivoTrue(email).isPresent()) {
            throw new IllegalArgumentException("Já existe um formulário ativo para este email."); // Exception específica em futura feature
        }
    }

    private FormularioPcd criarFormulario(FormSolicitacaoRequestDto requestDto) {
        verificarFormularioExistente(requestDto.email());
        usuarioService.verificarUsuarioExistente(requestDto.email());

        String comprovacaoId = salvarComprovacao(requestDto.comprovacao());
        FormularioPcd formulario = requestDto.toEntity(comprovacaoId);

        formulario.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return formulario;
    }

    private String salvarComprovacao(MultipartFile comprovacao) {
        if (comprovacao == null || comprovacao.isEmpty()) {
            throw new RuntimeException("Erro ao ler arquivo de comprovação."); // Exception específica em futura feature
        }
        String comprovacaoId = UUID.randomUUID().toString();

        Path uploadPath = Paths.get("upload");
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e){
            throw new RuntimeException("Erro ao salvar comprovacao."); // Exception específica em futura feature
        }

        Path comprovacaoPath = uploadPath.resolve(comprovacaoId);
        Files.write(comprovacaoPath, comprovacao.getBytes());

        return comprovacaoId;
    }

    private void atualizarSolicitacao(FormularioPcd formulario, FormSolicitacaoRequestDto requestDto) throws IOException {
        formulario.setNome(requestDto.nome());
        formulario.setEmail(requestDto.email());
        formulario.setSenha(requestDto.senha());
        formulario.setTiposDeficiencia(requestDto.tiposDeficiencia());
        formulario.setDesejaSuporte(requestDto.desejaSuporte());
        formulario.setComprovacaoId(salvarComprovacao(requestDto.comprovacao()));
    }

    private Administrador buscarAdministradorResponsavel() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalArgumentException("O usuário não está autenticado."); // Exception específica em futura feature
        }
        return adminService.procurarAdminAtivo(auth.getName());
    }

    private FormularioPcd procurarFormularioAtivo(String email) {
        return repository
                .findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new RuntimeException("Entidade não encontrada.")); // Exception específica em futura feature
    }

    private void atualizarAprovacao(FormularioPcd formulario, FormAprovacaoRequestDto requestDto, Administrador adminResponsavel) {
        formulario.setStatus(requestDto.aprovado() ? StatusFormulario.APROVADO : StatusFormulario.REPROVADO);
        formulario.setMotivoReprovacao(requestDto.motivoReprovacao());
        formulario.setAdminResponsavel(adminResponsavel);
        formulario.setAtivo(false);
    }
}
