package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.identificacao_pcd.IdentificacaoDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.pendencia_atendimento.PendenciaResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.tag_pcd.TagRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.IdentificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "Identificações", description = "Simulação de identificação de tags de PcD nas entradas das estações.")
@RestController
@RequestMapping("/api/identificacao")
@RequiredArgsConstructor
public class IdentificacaoController {
    private final IdentificacaoService service;

    @Operation(
            summary = "Identificar uma tag",
            description = "Dispara um evento para identificar a tag simulada.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload enviado pela tag",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TagRequestDto.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "codigoTag": "TAG-XYZ-789",
                                        "bssid": "34:AB:95:1F:2C:88",
                                        "embarque": true
                                    }
                                    """)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Identificação realizada."),
                    @ApiResponse(responseCode = "400", description = "Campos do payload não correspondem aos dados esperados.")
            }
    )
    @PostMapping
    public ResponseEntity<PendenciaResponseDto> simularIdentificacao(@RequestBody @Valid IdentificacaoDto dto) {
        return ResponseEntity
                .created(URI.create("api/simulacoes"))
                .body(service.solicitarPendencia(dto));
    }
}