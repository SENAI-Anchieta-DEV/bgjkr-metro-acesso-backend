package com.senai.bgjkr_metro_acesso_backend.application.service;

import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoDto;
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

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class FormularioService {
    private final FormularioRepository repository;
    private final AdminService adminService;
    private final TagService tagService;
    private final PcdService pcdService;

    @Transactional
    public FormSolicitacaoDto enviarFormulario(FormSolicitacaoDto requestDto) {
        repository.findByEmail(requestDto.email())
                .ifPresent(f -> {
                    throw new RuntimeException("Este e-mail já está vinculado a um formulário."); // Exception específica em futura feature
                });

        FormularioPcd formRegistrado = criarFormulario(requestDto);
        return FormSolicitacaoDto.fromEntity(repository.save(formRegistrado));
    }

    @Transactional
    public FormAprovacaoResponseDto aprovarFormulario(String email, FormAprovacaoRequestDto requestDto) {
        if (!requestDto.aprovado() && (requestDto.motivoReprovacao() == null || requestDto.motivoReprovacao().isBlank())) {
            throw new IllegalArgumentException("Motivo é obrigatório para reprovação"); // Exception específica em futura feature
        }

        FormularioPcd formAnalisado = procurarFormularioAtivo(email);
        Administrador adminResponsavel = buscarAdministradorResponsavel();

        atualizarValores(formAnalisado, requestDto, adminResponsavel);

        if (requestDto.aprovado()) {
            pcdService.registrarPcd(new PcdRequestDto(
                    formAnalisado.getNome(),
                    email,
                    formAnalisado.getSenha(),
                    new HashSet<>(formAnalisado.getTiposDeficiencia()),
                    formAnalisado.isDesejaSuporte(),
                    tagService.escolherTagDisponivel().getCodigoTag()
            ));
        }

        return FormAprovacaoResponseDto.fromEntity(formAnalisado);
    }

    private FormularioPcd criarFormulario(FormSolicitacaoDto requestDto) {
        FormularioPcd formulario = requestDto.toEntity();
        formulario.setSenha(requestDto.senha()); // Criptografia de senha em futura feature
        return formulario;
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

    private void atualizarValores(FormularioPcd formulario, FormAprovacaoRequestDto requestDto, Administrador adminResponsavel) {
        formulario.setStatus(requestDto.aprovado() ? StatusFormulario.APROVADO : StatusFormulario.REPROVADO);
        formulario.setMotivoReprovacao(requestDto.motivoReprovacao());
        formulario.setAdminResponsavel(adminResponsavel);
        formulario.setAtivo(false);
    }
}
