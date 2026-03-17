package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import com.senai.bgjkr_metro_acesso_backend.domain.enums.TipoDeficiencia;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "usuarios_pcd")
public class UsuarioPcd extends Usuario {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pcd_tipos_deficiencia", joinColumns = @JoinColumn(name = "pcd_id"))
    @Column(name = "tipoDeficiencia", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<TipoDeficiencia> tiposDeficiencia;

    @Column(nullable = false)
    private boolean desejaSuporte;

    @OneToOne
    @JoinColumn(name = "tag_id", unique = true)
    private TagPcd tag;
}