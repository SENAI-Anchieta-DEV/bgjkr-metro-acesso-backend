package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "tags",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "codigoTag")
        }
)
public class TagPcd {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(nullable = false, length = 50)
    private String codigoTag;

    @OneToOne(mappedBy = "tag")
    private UsuarioPcd usuarioPcd;

    @Column(nullable = false)
    private boolean ativo;
}