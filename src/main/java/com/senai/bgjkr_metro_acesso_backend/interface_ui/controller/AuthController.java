package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticação", description = "Geração de tokens e autenticação de usuários (PCDs, Agentes e Admins).")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @Operation(
            summary = "Realizar Login",
            description = "Valida as credenciais (email e senha) e retorna um Token JWT para uso nas requisições protegidas.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais do usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"email\": \"usuario@metroacesso.com\",\n  \"senha\": \"MinhaSenhaSecreta\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token retornado."),
                    @ApiResponse(responseCode = "400", description = "Campos obrigatórios ausentes ou fora do padrão."),
                    @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou não autorizadas.")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthRequestDto requestDto) {
        return ResponseEntity.ok(service.login(requestDto));
    }
}