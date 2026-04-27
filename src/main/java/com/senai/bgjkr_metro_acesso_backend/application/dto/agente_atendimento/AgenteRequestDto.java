package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

@Schema(description = "Objeto de transferência de dados para o registo e atualização de Agentes de Atendimento.")
public record AgenteRequestDto(
        @Schema(description = "Nome completo do agente.", example = "Carlos Souza")
        @NotBlank
        @Size(max = 200)
        String nome,

        @Schema(description = "E-mail de acesso corporativo.", example = "carlos.agente@metroacesso.com")
        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @Schema(description = "Senha de acesso ao sistema.", example = "SenhaAgente123")
        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @Schema(description = "Horário de início do turno no formato HH:mm:ss.", type = "string", format = "time", example = "06:00:00")
        @NotNull
        LocalTime inicioTurno,

        @Schema(description = "Horário de término do turno no formato HH:mm:ss.", type = "string", format = "time", example = "14:00:00")
        @NotNull
        LocalTime fimTurno,

        @Schema(description = "Código único da estação em que o agente atuará.", example = "EST-SE-01")
        @NotBlank
        @Size(max = 200)
        String codigoEstacao
) {
    public AgenteAtendimento toEntity(Estacao estacao) {
        return AgenteAtendimento.builder()
                .nome(nome)
                .email(email)
                .senha(senha)
                .ativo(true)
                .role(Role.AGENTE_ATENDIMENTO)
                .estacao(estacao)
                .inicioTurno(inicioTurno)
                .fimTurno(fimTurno)
                .build();
    }
}