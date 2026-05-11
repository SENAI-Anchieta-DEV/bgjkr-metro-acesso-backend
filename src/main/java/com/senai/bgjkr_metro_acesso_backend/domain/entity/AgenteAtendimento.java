package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "agentes_atendimento")
public class AgenteAtendimento extends Usuario {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacao_id")
    private Estacao estacao;

    @Column(nullable = false)
    private LocalTime inicioTurno;

    @Column(nullable = false)
    private LocalTime fimTurno;

    @OneToMany(mappedBy = "agente")
    private List<PendenciaAtendimento> pendencias;
}