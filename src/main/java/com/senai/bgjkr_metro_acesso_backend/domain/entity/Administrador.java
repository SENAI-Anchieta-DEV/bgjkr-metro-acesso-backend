package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "administradores")
public class Administrador extends Usuario {
    @OneToMany(mappedBy = "adminResponsavel")
    private List<FormularioPcd> formularios;
}