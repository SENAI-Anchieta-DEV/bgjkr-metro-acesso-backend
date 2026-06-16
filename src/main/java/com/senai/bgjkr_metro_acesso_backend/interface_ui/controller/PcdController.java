package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.comprovacao.ComprovacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdUpdateDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PcdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Usuários PCD", description = "Gestão das contas de pessoas com deficiência.")
@RestController
@RequestMapping("/api/pcd")
@RequiredArgsConstructor
public class PcdController {
    private final PcdService service;

    @Operation(
            summary = "Cadastrar usuário PCD (admin)",
            description = "Cadastro direto pelo administrador via multipart/form-data, incluindo laudo médico.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou comprovação ausente.")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PcdResponseDto> registrarPcd(@ModelAttribute @Valid PcdRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/pcd"))
                .body(service.registrarPcd(requestDto));
    }

    @Operation(
            summary = "Listar PCDs",
            description = "Retorna todos os usuários PCD ativos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listagem gerada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<PcdResponseDto>> listarPcdsAtivos() {
        return ResponseEntity.ok(service.listarPcdsAtivos());
    }

    @Operation(
            summary = "Buscar PCD por e-mail",
            description = "Localiza os dados de um PCD pelo endereço de e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do usuário PCD", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário localizado."),
                    @ApiResponse(responseCode = "404", description = "Usuário PCD não encontrado.")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<PcdResponseDto> buscarPcdAtivo(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarPcdAtivo(email));
    }

    @Operation(
            summary = "Atualizar PCD",
            description = "Atualiza nome, senha, suporte, tipos de deficiência e/ou comprovação. " +
                    "Todos os campos são opcionais. Enviar via multipart/form-data.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail atual do PCD", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com êxito."),
                    @ApiResponse(responseCode = "404", description = "Usuário não localizado.")
            }
    )
    @PutMapping(value = "/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PcdResponseDto> atualizarPcd(
            @PathVariable String email,
            @ModelAttribute PcdUpdateDto updateDto) {
        return ResponseEntity.ok(service.atualizarPcd(email, updateDto));
    }

    @Operation(
            summary = "Baixar comprovação de deficiência",
            description = "Retorna o arquivo de laudo médico vinculado ao usuário PCD. " +
                    "Acesso restrito a ADMINISTRADOR ou ao próprio usuário.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do usuário PCD", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso.",
                            content = @Content(mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string", format = "binary"))),
                    @ApiResponse(responseCode = "404", description = "Arquivo não encontrado.")
            }
    )
    @GetMapping("/{email}/comprovacao")
    public ResponseEntity<Resource> downloadComprovacao(@PathVariable String email) {
        ComprovacaoDto comprovacaoDto = service.downloadComprovacao(email);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(comprovacaoDto.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"comprovacao-" + comprovacaoDto.comprovacaoId() + "\"")
                .body(comprovacaoDto.resource());
    }

    @Operation(
            summary = "Remover PCD",
            description = "Inativa o cadastro de um PCD pelo e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail da conta a ser inativada", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário inativado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário PCD não encontrado.")
            }
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerPcd(@PathVariable String email) {
        service.removerPcd(email);
        return ResponseEntity.noContent().build();
    }
}