package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.sensor.SensorResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.SensorService;
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

@Tag(name = "Entradas", description = "Configuração das entradas nas estações.")
@RestController
@RequestMapping("/api/entrada")
@RequiredArgsConstructor
public class SensorController {
    private final SensorService service;

    @Operation(
            summary = "Registrar nova entrada",
            description = "Adiciona uma entrada a uma estação informando a porta física instalada.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Informações da entrada",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"codigoEstacao\": \"EST-SE-01\",\n  \"codigoEntrada\": \"ENTRADA-100X\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Entrada associada com sucesso à estação."),
                    @ApiResponse(responseCode = "400", description = "Código de estação inválido ou entrada já existente.")
            }
    )
    @PostMapping
    public ResponseEntity<SensorResponseDto> registrarSensor(@RequestBody @Valid SensorRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/entrada"))
                .body(service.registrarSensor(requestDto));
    }

    @Operation(
            summary = "Listar todas as entradas",
            description = "Retorna um mapeamento de todas as entradas cadastradas e suas estações correspondentes.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Busca retornada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<SensorResponseDto>> listarSensorsAtivos() {
        return ResponseEntity.ok(service.listarSensorsAtivos());
    }

    @Operation(
            summary = "Buscar entrada por código",
            description = "Verifica o status e o vínculo das entradas usando seu código.",
            parameters = {
                    @Parameter(name = "codigoEntrada", description = "Código da entrada", example = "ENTRADA-100X", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Detalhes das entradas encontradas."),
                    @ApiResponse(responseCode = "404", description = "Entrada não existe no cadastro.")
            }
    )
    @GetMapping("/{codigoEntrada}")
    public ResponseEntity<SensorResponseDto> buscarSensorAtivo(@PathVariable String codigoSensor) {
        return ResponseEntity.ok(service.buscarSensorAtivo(codigoSensor));
    }

    @Operation(
            summary = "Atualizar informações da entrada",
            description = "Atualiza os dados de uma entrada (ex: mudança de nome)",
            parameters = {
                    @Parameter(name = "codigoEntrada", description = "Código atual da entrada", example = "ENTRADA-100X", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novas informações de entrada",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SensorRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"codigoEstacao\": \"EST-SE-01\",\n  \"codigoEntrada\": \"ENTRADA-100X\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Informações da entrada atualizada com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Entrada a ser editada não existe.")
            }
    )
    @PutMapping("/{codigoEntrada}")
    public ResponseEntity<SensorResponseDto> atualizarSensor(@PathVariable String codigoSensor, @RequestBody @Valid SensorRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarSensor(codigoSensor, requestDto));
    }

    @Operation(
            summary = "Remover entrada",
            description = "Desativa/deleta uma entrada de uma estação.",
            parameters = {
                    @Parameter(name = "codigoEntrada", description = "Código da entrada para remoção", example = "ENTRADA-100X", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Entrada removida com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Entrada não encontrada para remoção.")
            }
    )
    @DeleteMapping("/{codigoEntrada}")
    public ResponseEntity<Void> removerSensor(@PathVariable String codigoSensor) {
        service.removerSensor(codigoSensor);
        return ResponseEntity.noContent().build();
    }
}