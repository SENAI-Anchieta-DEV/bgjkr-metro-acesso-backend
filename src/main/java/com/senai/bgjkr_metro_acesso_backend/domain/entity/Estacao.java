package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.Linha;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "estacoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "codigoEstacao")
        }
)
public class Estacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 120)
    private String codigoEstacao;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "estacao_linhas", joinColumns = @JoinColumn(name = "estacao_id"))
    @Column(name = "linha")
    @Enumerated(EnumType.STRING)
    private Set<Linha> linhas;

    @OneToMany(mappedBy = "estacao")
    private List<AgenteAtendimento> agentes;

    @OneToMany(mappedBy = "estacao")
    private List<Entrada> entradas;

    @OneToMany(mappedBy = "estacao")
    private List<PendenciaAtendimento> pendencias;

    @Column(nullable = false)
    private boolean ativo;
}