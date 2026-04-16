package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.authentication.AuthResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Autenticação do usuário no sistema.")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @Operation(summary = "Realizar login",
            description = "Autentica um usuário no sistema e retorna o token JWT necessário para os demais endpoints.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Login realizado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))),

            @ApiResponse(responseCode = "400",
                    description = "Dados da requisição inválidos (Ex: campos vazios).",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),

            @ApiResponse(responseCode = "401",
                    description = "Credenciais inválidas (E-mail ou senha incorretos).",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Credenciais do usuário para login")
            @RequestBody @Valid AuthRequestDto requestDto) {

        return ResponseEntity.ok(service.login(requestDto));
    }
}