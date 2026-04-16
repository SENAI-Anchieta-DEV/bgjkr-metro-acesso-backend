package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.estacao.EstacaoResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.EstacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Estações", description = "Gestão das estações do metrô e suas linhas pertencentes.")
@RestController
@RequestMapping("/api/estacao")
@RequiredArgsConstructor
public class EstacaoController {
    private final EstacaoService service;

    @Operation(
            summary = "Cadastrar uma nova estação",
            description = "Registra uma nova estação informando seu nome, código único e o conjunto numérico das linhas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Detalhes da estação",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstacaoRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Sé\",\n  \"codigoEstacao\": \"EST-SE-01\",\n  \"linhas\": [1, 3]\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Estação criada com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Erro de validação (ex: linha inválida ou array vazio).")
            }
    )
    @PostMapping
    public ResponseEntity<EstacaoResponseDto> registrarEstacao(@RequestBody @Valid EstacaoRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/estacao"))
                .body(service.registrarEstacao(requestDto));
    }

    @Operation(
            summary = "Listar todas as estações",
            description = "Recupera todas as estações cadastradas e ativas do sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listagem de estações retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<EstacaoResponseDto>> listarEstacoesAtivas() {
        return ResponseEntity.ok(service.listarEstacoesAtivas());
    }

    @Operation(
            summary = "Buscar estação por código",
            description = "Recupera detalhes técnicos de uma estação utilizando o seu código identificador.",
            parameters = {
                    @Parameter(name = "codigo", description = "Código único da estação", example = "EST-SE-01", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estação localizada."),
                    @ApiResponse(responseCode = "404", description = "Nenhuma estação encontrada com o código informado.")
            }
    )
    @GetMapping("/{codigo}")
    public ResponseEntity<EstacaoResponseDto> buscarEstacaoAtiva(@PathVariable String codigo) {
        return ResponseEntity.ok(service.buscarEstacaoAtiva(codigo));
    }

    @Operation(
            summary = "Atualizar uma estação",
            description = "Atualiza os dados de uma estação (ex: mudança de nome ou inclusão de novas linhas conectadas).",
            parameters = {
                    @Parameter(name = "codigo", description = "Código da estação a ser atualizada", example = "EST-SE-01", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados da estação",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EstacaoRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Sé (Integração)\",\n  \"codigoEstacao\": \"EST-SE-01\",\n  \"linhas\": [1, 3]\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Estação atualizada com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Erro na validação do Payload."),
                    @ApiResponse(responseCode = "404", description = "Estação não encontrada.")
            }
    )
    @PutMapping("/{codigo}")
    public ResponseEntity<EstacaoResponseDto> atualizarEstacao(@PathVariable String codigo, @RequestBody @Valid EstacaoRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarEstacao(codigo, requestDto));
    }

    @Operation(
            summary = "Remover uma estação",
            description = "Remove o registro de uma estação do sistema pelo seu código.",
            parameters = {
                    @Parameter(name = "codigo", description = "Código da estação a ser removida", example = "EST-SE-01", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Estação removida com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Estação não encontrada.")
            }
    )
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> removerEstacao(@PathVariable String codigo) {
        service.removerEstacao(codigo);
        return ResponseEntity.noContent().build();
    }
}