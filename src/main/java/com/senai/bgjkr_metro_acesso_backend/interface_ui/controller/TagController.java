package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.TagService;
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

@Tag(name = "Tags", description = "Gerenciamento de tags utilizadas pelas PCDs.")
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {
    private final TagService service;

    @Operation(
            summary = "Cadastrar Tag",
            description = "Armazena o código de uma nova Tag no sistema.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Código da tag",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"codigoTag\": \"TAG-XYZ-789\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tag criada e registrada."),
                    @ApiResponse(responseCode = "400", description = "Código de tag cadastrado no banco de dados.")
            }
    )
    @PostMapping
    public ResponseEntity<TagResponseDto> registrarTag(@RequestBody @Valid TagRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/tag"))
                .body(service.registrarTag(requestDto));
    }

    @Operation(
            summary = "Listar Tags cadastradas",
            description = "Recupera todas as tags lançadas no banco, indicando se elas possuem ou não um usuário associado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Leitura das tags com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<TagResponseDto>> listarTagsAtivas() {
        return ResponseEntity.ok(service.listarTagsAtivas());
    }

    @Operation(
            summary = "Buscar Tag por código",
            description = "Confere se uma Tag específica existe e exibe suas dependências.",
            parameters = {
                    @Parameter(name = "codigoTag", description = "Código da Tag", example = "TAG-XYZ-789", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "A tag foi localizada no sistema."),
                    @ApiResponse(responseCode = "404", description = "Código inexistente.")
            }
    )
    @GetMapping("/{codigoTag}")
    public ResponseEntity<TagResponseDto> buscarTagAtiva(@PathVariable String codigoTag) {
        return ResponseEntity.ok(service.buscarTagAtiva(codigoTag));
    }

    @Operation(
            summary = "Atualizar informações da Tag",
            description = "Substitui o código de uma Tag.",
            parameters = {
                    @Parameter(name = "codigoTag", description = "Código a ser modificado", example = "TAG-XYZ-789", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "O novo identificador",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"codigoTag\": \"TAG-NOVA-888\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Código renomeado/atualizado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Tag não pôde ser encontrada.")
            }
    )
    @PutMapping("/{codigoTag}")
    public ResponseEntity<TagResponseDto> atualizarTag(@PathVariable String codigoTag, @RequestBody @Valid TagRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarTag(codigoTag, requestDto));
    }

    @Operation(
            summary = "Desativar/Excluir Tag",
            description = "Remove a Tag, geralmente em caso de perda, bloqueio ou devolução sem reutilização.",
            parameters = {
                    @Parameter(name = "codigoTag", description = "Identificador da Tag corrompida/devolvida", example = "TAG-XYZ-789", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "A tag foi removida."),
                    @ApiResponse(responseCode = "404", description = "Tag não listada no banco de dados.")
            }
    )
    @DeleteMapping("/{codigoTag}")
    public ResponseEntity<Void> removerTag(@PathVariable String codigoTag) {
        service.removerTag(codigoTag);
        return ResponseEntity.noContent().build();
    }
}