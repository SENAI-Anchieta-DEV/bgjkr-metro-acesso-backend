package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Entrada;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntradaRepository extends JpaRepository<Entrada, String> {
    Optional<Entrada> findByCodigoEntrada(String codigoEntrada);

    List<Entrada> findAllByEstacao(Estacao estacao);

    Optional<Entrada> findByCodigoEntradaAndAtivoTrue(String codigoEntrada);

    List<Entrada> findAllByAtivoTrue();
}
