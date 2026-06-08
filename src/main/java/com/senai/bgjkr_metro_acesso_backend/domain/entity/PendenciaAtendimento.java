package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "pendencias")
public class PendenciaAtendimento {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne
    @JoinColumn(name = "pcd_atendido_id")
    private UsuarioPcd pcdAtendido;

    @ManyToOne
    @JoinColumn(name = "agente_id")
    private AgenteAtendimento agente;

    @ManyToOne
    @JoinColumn(name = "estacao_id")
    private Estacao estacao;

    @ManyToOne
    @JoinColumn(name = "entrada_id")
    private Entrada entrada;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private boolean ativo;
}
