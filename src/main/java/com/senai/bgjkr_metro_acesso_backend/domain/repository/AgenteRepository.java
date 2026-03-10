package com.senai.bgjkr_metro_acesso_backend.domain.repository;

import com.senai.bgjkr_metro_acesso_backend.domain.entity.AgenteAtendimento;
import com.senai.bgjkr_metro_acesso_backend.domain.entity.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgenteRepository extends JpaRepository<AgenteAtendimento, String> {
    List<AgenteAtendimento> findAllByEstacao(Estacao estacao);

    Optional<AgenteAtendimento> findByEmailAndAtivoTrue(String email);

    Optional<AgenteAtendimento> findByEmail(String email);

    Optional<AgenteAtendimento> findByIdAndAtivoTrue(String id);

    List<AgenteAtendimento> findAllByAtivoTrue();
}