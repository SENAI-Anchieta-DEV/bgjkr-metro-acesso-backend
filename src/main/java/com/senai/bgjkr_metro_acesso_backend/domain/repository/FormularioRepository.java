package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.FormularioPcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormularioRepository extends JpaRepository<FormularioPcd, String> {
    Optional<FormularioPcd> findByEmail(String email);

    List<FormularioPcd> findAllByAtivoTrue();

    Optional<FormularioPcd> findByEmailAndAtivoTrue(String email);
}