package com.senai.bgjkr_metro_acesso_backend.interface_ui.controller;

import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdRequestDto;
import com.senai.bgjkr_metro_acesso_backend.application.dto.usuario_pcd.PcdResponseDto;
import com.senai.bgjkr_metro_acesso_backend.application.service.PcdService;
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

@Tag(name = "Usuários PCD", description = "Gestão das contas de pessoas com deficiência após o formulário aprovado.")
@RestController
@RequestMapping("/api/pcd")
@RequiredArgsConstructor
public class PcdController {
    private final PcdService service;

    @Operation(
            summary = "Cadastrar usuário PCD",
            description = "Efetiva a criação do usuário PCD, vinculando suas deficiências e a Tag associada.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário PCD",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PcdRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Felipe Costa\",\n  \"email\": \"felipe.pcd@metroacesso.com\",\n  \"senha\": \"MinhaSenhaSegura123\",\n  \"tiposDeficiencia\": [\"VISUAL\", \"MOTORA\"],\n  \"desejaSuporte\": true,\n  \"codigoTag\": \"TAG-XYZ-789\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Tag indisponível ou dados de usuário incorretos.")
            }
    )
    @PostMapping
    public ResponseEntity<PcdResponseDto> registrarPcd(@RequestBody @Valid PcdRequestDto requestDto) {
        return ResponseEntity
                .created(URI.create("api/pcd"))
                .body(service.registrarPcd(requestDto));
    }

    @Operation(
            summary = "Listar PCDs",
            description = "Recupera uma listagem contendo todos os usuários PCD ativos cadastrados.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listagem gerada com sucesso.")
            }
    )
    @GetMapping
    public ResponseEntity<List<PcdResponseDto>> listarPcdsAtivos() {
        return ResponseEntity.ok(service.listarPcdsAtivos());
    }

    @Operation(
            summary = "Buscar PCD por e-mail",
            description = "Localiza os dados de um PCD cadastrado com base no endereço de e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail do usuário PCD", example = "felipe.pcd@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário localizado."),
                    @ApiResponse(responseCode = "404", description = "Usuário PCD não encontrado.")
            }
    )
    @GetMapping("/{email}")
    public ResponseEntity<PcdResponseDto> buscarPcdAtivo(@PathVariable String email) {
        return ResponseEntity.ok(service.buscarPcdAtivo(email));
    }

    @Operation(
            summary = "Atualizar PCD",
            description = "Permite alterar nome, senha, requisição de suporte ou trocar o código da tag atrelada.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail de identificação atual do PCD", example = "felipe.pcd@metroacesso.com", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados para atualização",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PcdRequestDto.class),
                            examples = @ExampleObject(value = "{\n  \"nome\": \"Felipe Costa Santos\",\n  \"email\": \"felipe.pcd@metroacesso.com\",\n  \"senha\": \"NovaSenhaSegura123\",\n  \"tiposDeficiencia\": [\"VISUAL\"],\n  \"desejaSuporte\": false,\n  \"codigoTag\": \"TAG-NOVA-000\"\n}")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com êxito."),
                    @ApiResponse(responseCode = "400", description = "Nova Tag solicitada já se encontra em uso por outro usuário."),
                    @ApiResponse(responseCode = "404", description = "Usuário não localizado.")
            }
    )
    @PutMapping("/{email}")
    public ResponseEntity<PcdResponseDto> atualizarPcd(@PathVariable String email, @RequestBody @Valid PcdRequestDto requestDto) {
        return ResponseEntity.ok(service.atualizarPcd(email, requestDto));
    }

    @Operation(
            summary = "Remover PCD",
            description = "Inativa o cadastro de um PCD através do seu endereço de e-mail.",
            parameters = {
                    @Parameter(name = "email", description = "E-mail da conta a ser inativada", example = "felipe.pcd@metroacesso.com", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário inativado/removido com sucesso."),
                    @ApiResponse(responseCode = "404", description = "Usuário PCD não encontrado.")
            }
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> removerPcd(@PathVariable String email) {
        service.removerPcd(email);
        return ResponseEntity.noContent().build();
    }
}