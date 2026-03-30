package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import com.senai.bgjkr_metro_acesso_backend.domain.enums.StatusFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioPcd, String> {
    Optional<FormularioPcd> findByEmail(String email);

    List<FormularioPcd> findAllByAtivoTrue();

    List<FormularioPcd> findAllByStatusAndAtivoTrue(StatusFormulario status);

    Optional<FormularioPcd> findByEmailAndAtivoTrue(String email);
}