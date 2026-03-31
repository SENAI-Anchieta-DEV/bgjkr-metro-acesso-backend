package com.senai.bgjkr_metro_acesso_backend.application.dto.agente_atendimento;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record AgenteRequestDto(
        @NotBlank
        @Size(max = 200)
        String nome,

        @NotBlank
        @Email
        @Size(max = 200)
        String email,

        @NotBlank
        @Size(min = 8, max = 200)
        String senha,

        @NotNull
        LocalTime inicioTurno,

        @NotNull
        LocalTime fimTurno,

        @NotBlank
        @Size(max = 200)
        String codigoEstacao
) {
    public AgenteAtendimento toEntity(
            Estacao estacao
    ) {
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
