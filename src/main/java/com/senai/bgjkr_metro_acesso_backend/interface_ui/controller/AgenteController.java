package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento.AgenteResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AgenteService;
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

@Tag(name = "Agentes de Atendimento", description = "Gerenciamento dos agentes vinculados às estações do metrô.")
@RestController
@RequestMapping("/api/agente")
@RequiredArgsConstructor
public class AgenteController {
    private final AgenteService service;

    @Operation(
            summary = "Cadastrar um novo agente",
            description = "Cria um novo agente de atendimento, definindo seu turno e a estação em que atua.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo agente",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgenteRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Carlos Souza\",\n  \"email\": \"carlos.agente@metroacesso.com\",\n  \"senha\": \"SenhaAgente123\",\n  \"inicioTurno\": \"06:00:00\",\n  \"fimTurno\": \"14:00:00\",\n  \"codigoEstacao\": \"EST-1029\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Agente cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Falha na validação dos dados (ex: estação inexistente).")
            }
    )
    @PostMapping
    public ResponseEntity<AgenteResponseDto> registrarAgente(@RequestBody @Valid AgenteRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/agente"))
                .body(service.registrarAgente(requestDto));
    }

    @Operation(
            summary = "Listar todos os agentes",
            description = "Retorna uma lista com todos os agentes de atendimento ativos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<AgenteResponseDto>> listarAgentesAtivos() {
        return ResponseEntity.ok(service.listarAgentesAtivos());
    }

    @Operation(
            summary = "Buscar agente por e-mail",
            description = "Recupera as informações de um agente específico através do seu e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "Endereço de e-mail do agente", example = "carlos.agente@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agente encontrado com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Nenhum agente encontrado com este e-mail.")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<AgenteResponseDto> buscarAgenteAtivo(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarAgenteAtivo(email));
    }

    @Operation(
            summary = "Atualizar um agente",
            description = "Atualiza as informações de um agente, como nome, turnos ou transferência de estação.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail atual do agente", example = "carlos.agente@metroacesso.com", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados do agente",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgenteRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Carlos Souza Atualizado\",\n  \"email\": \"carlos.agente@metroacesso.com\",\n  \"senha\": \"NovaSenha123\",\n  \"inicioTurno\": \"14:00:00\",\n  \"fimTurno\": \"22:00:00\",\n  \"codigoEstacao\": \"EST-5555\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Agente atualizado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Erro de validação (horários inválidos, formato errado)."),
                    @ApiResponse(responseCode = "404", description = "Agente não encontrado.")
            }
    )
    @PutMapping("/{email}")
    public ResponseEntity<AgenteResponseDto> atualizarAgente(@PathVariable String email, @RequestBody @Valid AgenteRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarAgente(email, requestDto));
    }

    @Operation(
            summary = "Remover um agente",
            description = "Desativa/remove o registro de um agente pelo seu e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do agente a ser removido", example = "carlos.agente@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Agente removido com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Agente não encontrado.")
            }
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerAgente(@PathVariable String email) {
        service.removerAgente(email);
        return ResponseEntity.noContent().build();
    }
}