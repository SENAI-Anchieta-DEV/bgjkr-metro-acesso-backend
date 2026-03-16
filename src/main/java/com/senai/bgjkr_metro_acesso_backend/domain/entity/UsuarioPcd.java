package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "usuarios_pcd")
public class UsuarioPcd extends Usuario {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeficiencia tipoDeficiencia;

    @Column(nullable = false)
    private boolean desejaSuporte;

    @OneToOne(optional = false)
    @JoinColumn(name = "tag_id", nullable = false, unique = true)
    private TagPcd tag;
}