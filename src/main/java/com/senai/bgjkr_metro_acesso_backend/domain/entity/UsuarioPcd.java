package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

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
public class UsuarioPcd extends Usuario {
    @Column(nullable = false)
    private String tipoNecessidade;

    @Column(nullable = false)
    private boolean desejaSuporte;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id")
    @JsonManagedReference
    private TagPcd tag;
}