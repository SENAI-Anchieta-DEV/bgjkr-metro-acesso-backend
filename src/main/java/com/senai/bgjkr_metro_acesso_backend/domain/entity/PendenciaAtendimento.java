package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusAtendimento;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    
    private LocalDateTime dataHora;

    private StatusAtendimento statusAtendimento;
}
