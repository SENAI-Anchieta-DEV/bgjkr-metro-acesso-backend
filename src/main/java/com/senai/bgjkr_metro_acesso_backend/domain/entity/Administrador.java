package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
@Table(name = "administradores")
public class Administrador extends Usuario {
}