package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Administrador, String> {
    Optional<Administrador> findByEmailAndAtivoTrue(String email);

    Optional<Administrador> findByEmail(String email);

    List<Administrador> findAllByAtivoTrue();
}