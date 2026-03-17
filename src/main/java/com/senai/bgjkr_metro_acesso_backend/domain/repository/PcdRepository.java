package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.UsuarioPcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PcdRepository extends JpaRepository<UsuarioPcd, String> {
    Optional<UsuarioPcd> findByEmail(String email);

    List<UsuarioPcd> findAllByAtivoTrue();

    Optional<UsuarioPcd> findByEmailAndAtivoTrue(String email);
}