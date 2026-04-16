package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.administrador.AdminResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AdminService;
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

@Tag(name = "Administradores", description = "Gerenciamento de contas de administradores do sistema.")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @Operation(
            summary = "Cadastrar um novo administrador",
            description = "Cria um novo administrador na base de dados utilizando nome, email e senha.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo administrador",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Maria Silva\",\n  \"email\": \"maria.admin@metroacesso.com\",\n  \"senha\": \"SenhaForte123!\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Administrador cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Falha na validação dos dados enviados.")
            }
    )
    @PostMapping
    public ResponseEntity<AdminResponseDto> registrarAdmin(@RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/admin"))
                .body(service.registrarAdmin(requestDto));
    }

    @Operation(
            summary = "Listar todos os administradores",
            description = "Retorna uma lista contendo todos os administradores ativos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> listarAdminsAtivos() {
        return ResponseEntity.ok(service.listarAdminsAtivos());
    }

    @Operation(
            summary = "Buscar administrador por e-mail",
            description = "Recupera os dados de um administrador específico a partir do seu endereço de e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "Endereço de e-mail do administrador", example = "maria.admin@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador encontrado."),
                    @ApiResponse(responseCode = "404", description = "Nenhum administrador encontrado com o e-mail informado.")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<AdminResponseDto> buscarAdminAtivo(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarAdminAtivo(email));
    }

    @Operation(
            summary = "Atualizar um administrador",
            description = "Atualiza os dados (nome, email, senha) de um administrador existente a partir do e-mail atual.",
            parameters = {
                    @Parameter(name = "email", description = "Endereço de e-mail atual do administrador", example = "maria.admin@metroacesso.com", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados do administrador",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Maria Silva Atualizada\",\n  \"email\": \"maria.nova@metroacesso.com\",\n  \"senha\": \"NovaSenha321!\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador atualizado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Falha na validação dos dados enviados."),
                    @ApiResponse(responseCode = "404", description = "Administrador não encontrado.")
            }
    )
    @PutMapping("/{email}")
    public ResponseEntity<AdminResponseDto> atualizarAdmin(@PathVariable String email, @RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarAdmin(email, requestDto));
    }

    @Operation(
            summary = "Remover um administrador",
            description = "Desativa/remove um administrador da base de dados usando o e-mail como identificador.",
            parameters = {
                    @Parameter(name = "email", description = "Endereço de e-mail do administrador a ser removido", example = "maria.admin@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Administrador removido com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Administrador não encontrado.")
            }
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerAdmin(@PathVariable String email) {
        service.removerAdmin(email);
        return ResponseEntity.noContent().build();
    }
}