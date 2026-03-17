package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstacaoRepository extends JpaRepository<Estacao, String> {
    List<Estacao> findAllByAtivoTrue();

    Optional<Estacao> findByCodigoEstacao(String codigoEstacao);

    Optional<Estacao> findByCodigoEstacaoAndAtivoTrue(String codigoEstacao);
}