package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.comprovacao.ComprovacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormAprovacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.formulario_pcd.FormSolicitacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.FormularioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Formulários PCD", description = "Submissão, validação e listagem de formulários de cadastro para PCDs.")
@RestController
@RequestMapping("/api/formulario")
@RequiredArgsConstructor
public class FormularioController {
    private final FormularioService service;

    @Operation(
            summary = "Registrar novo formulário de solicitação",
            description = "Envia uma solicitação de cadastro de PCD. Requer envio de dados (multipart/form-data) incluindo arquivo de comprovação.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Formulário submetido com sucesso e status em análise."),
                    @ApiResponse(responseCode = "400", description = "Tipos de deficiência inválidos ou falha no anexo de comprovação.")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FormSolicitacaoResponseDto> registrarFormulario(@ModelAttribute @Valid FormSolicitacaoRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/formulario"))
                .body(service.registrarFormulario(requestDto));
    }

    @Operation(
            summary = "Listar formulários validados",
            description = "Retorna todos os formulários que já passaram pelo processo de validação (aprovados/reprovados).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<FormAprovacaoResponseDto>> listarFormulariosAtivos() {
        return ResponseEntity.ok(service.listarFormulariosAtivos());
    }

    @Operation(
            summary = "Listar formulários pendentes",
            description = "Retorna todos os formulários submetidos que ainda estão com o status 'EM_ANALISE'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
            }
    )
    @GetMapping("/pendentes")
    public ResponseEntity<List<FormSolicitacaoResponseDto>> listarFormulariosPendentes() {
        return ResponseEntity.ok(service.listarFormulariosPendentes());
    }

    @Operation(
            summary = "Buscar formulário por e-mail",
            description = "Recupera os detalhes de um formulário já avaliado pelo e-mail do solicitante.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail utilizado na submissão", example = "usuario.pcd@gmail.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Formulário localizado."),
                    @ApiResponse(responseCode = "404", description = "Formulário não encontrado.")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<FormAprovacaoResponseDto> buscarFormularioAtivo(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarFormularioAtivo(email));
    }

    @Operation(
            summary = "Atualizar formulário pendente",
            description = "Permite corrigir informações ou re-enviar a comprovação para um formulário que ainda está pendente.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do formulário a ser atualizado", example = "usuario.pcd@gmail.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Formulário atualizado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Falha de validação ou formulário já avaliado."),
                    @ApiResponse(responseCode = "404", description = "Formulário não encontrado.")
            }
    )
    @PutMapping(path = "/pendentes/{email}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FormSolicitacaoResponseDto> atualizarFormularioPendente(@PathVariable String email, @ModelAttribute @Valid FormSolicitacaoRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarFormularioPendente(email, requestDto));
    }

    @Operation(
            summary = "Remover formulário pendente",
            description = "Cancela a solicitação de um formulário em análise.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail da solicitação a ser removida", example = "usuario.pcd@gmail.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Formulário removido com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Tentativa de remoção de formulário que já não está mais pendente."),
                    @ApiResponse(responseCode = "404", description = "Formulário não encontrado.")
            }
    )
    @DeleteMapping("/pendentes/{email}")
    public ResponseEntity<Void> removerFormularioPendente(@PathVariable String email) {
        service.removerFormularioPendente(email);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Validar um formulário",
            description = "Endpoint destinado a administradores para aprovar ou reprovar um formulário pendente, justificando o motivo caso negado.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do formulário a ser avaliado", example = "usuario.pcd@gmail.com", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Decisão da validação",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FormAprovacaoRequestDto.class),
                            examples = {
                                    @ExampleObject(name = "Aprovação", value = "{\n  \"aprovado\": true,\n  \"motivoReprovacao\": null\n}"),
                                    @ExampleObject(name = "Reprovação", value = "{\n  \"aprovado\": false,\n  \"motivoReprovacao\": \"Documento anexado ilegível.\"\n}")
                            }
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Validação registrada com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Motivo de reprovação ausente quando aprovado=false."),
                    @ApiResponse(responseCode = "404", description = "Formulário pendente não encontrado.")
            }
    )
    @PostMapping("/validar/{email}")
    public ResponseEntity<FormAprovacaoResponseDto> validarFormulario(@PathVariable String email, @RequestBody @Valid FormAprovacaoRequestDto requestDto) {
        return ResponseEntity.ok(service.validarFormulario(email, requestDto));
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
}