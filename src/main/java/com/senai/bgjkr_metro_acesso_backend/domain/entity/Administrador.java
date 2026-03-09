package com.senai.bgjkr_metro_acesso_backend.domain.entity;

import jakarta.persistence.Entity;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@SuperBuilder
public class Administrador extends Usuario {
}