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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Gerentes", description = "Gerenciamento de gerentes do banco.")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    // CREATE
    @Operation(
            summary = "Cadastrar um novo administrador",
            description = "Adiciona um novo administrador à base de dados após validações de nome, endereço de e-mail" +
                    " e senha.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdminResponseDto.class),
                            examples = @ExampleObject(name = "Exemplo válido", value = """
                                        {
                                          "nome": "José Silva dos Santos",
                                          "email": "jose@metroacesso.com",
                                          "senha": "JoseDosSantos1234"
                                        }
                                    """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Administrador cadastrado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Nome inválido",
                                                    value = "\"O nome deve ter entre 3 e 100 caracteres.\""),
                                            @ExampleObject(
                                                    name = "Email inválido",
                                                    value = "\"O campo preenchido deve ter formato de email válido.\"")
                                    }
                            )
                    )
            }
    )

    @PostMapping
    public ResponseEntity<AdminResponseDto> registrarAdmin(@RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/admin"))
                .body(service.registrarAdmin(requestDto));
    }

    // READ
    @Operation(
            summary = "Listar todos os Administradores",
            description = "Retorna todos os Administradores cadastrados na base de dados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso."
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> listarAdminsAtivos() {
        return ResponseEntity
                .ok(service.listarAdminsAtivos());
    }

    @Operation(
            summary = "Buscar Administrador por Email",
            description = "Retorna um administrador cadastrado na base de dados a partir do seu Email.",
            parameters = {
                    @Parameter(name = "email", description = "email do administrador a ser buscado", example = "admin@metroacesso.com")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador encontrado."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Administrador não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Administrador com email admin@metroacesso.com não encontrado.\"")
                            )
                    )
            }
    )

    @GetMapping("/{email}")
    public ResponseEntity<AdminResponseDto> buscarAdminAtivo(@PathVariable String email) {
        return ResponseEntity
        .ok(service.buscarAdminAtivo(email));
    }

    // UPDATE
    @Operation(
            summary = "Atualizar um Administrador",
            description = "Atualiza os dados de um administrador existente com novas informações.",
            parameters = {
                    @Parameter(name = "email", description = "Email do administrador a ser atualizado", example = "admin@metroacesso.com")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdminRequestDto.class),
                            examples = @ExampleObject(name = "Exemplo de atualização", value = """
                                    {
                                        "nome": "José Silva dos Santos",
                                        "email": "jose@metroacesso.com",
                                        "senha": "JoseDosSantos1234"
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Administrador atualizado com sucesso."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Erro de validação.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = {
                                            @ExampleObject(
                                                    name = "Preço inválido",
                                                    value = "\"Preço mínimo do serviço deve ser R$ 50,00\""),
                                            @ExampleObject(
                                                    name = "Duração excedida",
                                                    value = "\"Duração do serviço não pode exceder 30 dias\"")
                                    }
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Gerente não encontrado.",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "\"Gerente com ID 19876543210 não encontrado.\"")
                            )
                    )
            }
    )
    @PutMapping("/{email}")
    public ResponseEntity<AdminResponseDto> atualizarAdmin(@PathVariable String email, @RequestBody @Valid AdminRequestDto requestDto) {
        return ResponseEntity
        .ok(service.atualizarAdmin(email, requestDto));
    }

    // DELETE
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerAdmin(@PathVariable String email) {
        service.removerAdmin(email);
        return ResponseEntity
                .noContent()
                .build();

    }
}