package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "sensores",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "codigoSensor")
        }
)
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estacao_id", nullable = false)
    private Estacao estacao;

    @Column(nullable = false, length = 50)
    private String codigoSensor;

    @Column
    private String porta;

    @Column(nullable = false)
    private boolean ativo;
}